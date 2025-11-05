package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.auth.AuthenticationRequest;
import tasknavigation.demo.auth.AuthenticationResponse;
import tasknavigation.demo.auth.AuthenticationService;
import tasknavigation.demo.auth.RegisterRequest;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.domain.enums.NivelAcesso;
import tasknavigation.demo.service.EmailService;
import tasknavigation.demo.service.EquipeService;
import tasknavigation.demo.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {
        "http://localhost:5500",   // Flutter Web
        "http://localhost:5173",   // Painel admin (React/Vite)
        "http://127.0.0.1:5500",   // Live Server
        "http://172.19.0.134:5500" // Mobile Screen Extension
})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationService authenticationService;

    /** Listar todos os usuários */
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuario());
    }

    /** Criar nova conta */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        String email = (String) body.get("email");
        String senha = (String) body.get("senha");
        String nivel = (String) body.getOrDefault("nivel", "USUARIO");

        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(email);
        if (usuarioExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha)); // encode ativado

        if (nivel.equals("USUARIO")) {
            usuario.setNivelAcesso(NivelAcesso.USUARIO);
        } else if (nivel.equals("ADMIN")) {
            usuario.setNivelAcesso(NivelAcesso.ADMIN);
        }

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(usuario.getEmail());
        registerRequest.setPassword(senha);
        registerRequest.setNome(usuario.getNome());
        registerRequest.setNivelAcesso(usuario.getNivelAcesso());

        var novoUsuario = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    /** Enviar código de recuperação */
    @PostMapping("/enviar-codigo-recuperacao")
    public ResponseEntity<String> enviarCodigoRecuperacao(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = usuarioOpt.get();
        String codigo = UUID.randomUUID().toString().substring(0, 6);
        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoExpiracao(LocalDateTime.now().plusMinutes(15));
        usuarioService.salvar(usuario);

        emailService.enviarCodigoRecuperacao(usuario.getEmail(), codigo);
        return ResponseEntity.ok("Código enviado para o e-mail.");
    }

    /** Verificar código de recuperação */
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = usuarioOpt.get();
        if (!codigo.equals(usuario.getCodigoRecuperacao()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido.");
        if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código expirado.");

        return ResponseEntity.ok("Código válido.");
    }

    /** Redefinir senha */
    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");
        String novaSenha = body.get("novaSenha");

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = usuarioOpt.get();

        if (!codigo.equals(usuario.getCodigoRecuperacao()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido.");
        if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código expirado.");

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoExpiracao(null);
        usuarioService.salvar(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    /** Login com JWT */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        try {
            String email = (String) body.get("email");
            String senha = (String) body.get("senha");
            Boolean isWebLogin = (Boolean) body.getOrDefault("isWebLogin", false);

            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setEmail(email);
            authenticationRequest.setPassword(senha);

            AuthenticationResponse authResponse;

            // Verifica se é login Web ou Mobile
            if (Boolean.TRUE.equals(isWebLogin)) {
                authResponse = authenticationService.authenticate(authenticationRequest);
            } else {
                authResponse = authenticationService.authenticate(authenticationRequest, true);
            }

            return ResponseEntity.ok(authResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    /** Listar usuários da equipe do admin logado */
    @GetMapping("/equipe/{equipeId}")
    public ResponseEntity<?> listarUsuariosPorEquipe(@PathVariable Long equipeId) {
        List<Usuario> usuarios = usuarioService.buscarPorEquipe(equipeId);
        return ResponseEntity.ok(usuarios);
    }

    /** Buscar usuário pelo ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        return ResponseEntity.ok(usuarioOpt.get());
    }

    /** Aceitar convite para equipe */
    @PostMapping("/aceitar-convite")
    public ResponseEntity<?> aceitarConvite(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigoConvite = body.get("codigoConvite");

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");

        Usuario usuario = usuarioOpt.get();

        Optional<Equipe> equipeOpt = equipeService.buscarPorCodigoConvite(codigoConvite);
        if (equipeOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de convite inválido");

        usuario.setEquipe(equipeOpt.get());
        usuarioService.salvar(usuario);

        return ResponseEntity.ok("Convite aceito com sucesso!");
    }
}
