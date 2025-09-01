package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.service.EmailService;
import tasknavigation.demo.service.EquipeService;
import tasknavigation.demo.service.UsuarioService;
import tasknavigation.demo.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {
    "http://localhost:59186",  // Flutter Web
    "http://localhost:5173",   
    "http://10.0.2.2:8080",    
    "http://192.168.56.1:8080"
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
        Boolean isWebLogin = (Boolean) body.getOrDefault("isWebLogin", false);

        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(email);
        if (usuarioExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setNivelAcesso(isWebLogin ? "ADMIN" : "USUARIO");

        Usuario novoUsuario = usuarioService.salvar(usuario);
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
        String novaSenha = body.get("novaSenha");
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoExpiracao(null);
        usuarioService.salvar(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    /** Login com JWT */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String senha = (String) body.get("senha");
        Boolean isWebLogin = (Boolean) body.getOrDefault("isWebLogin", false);

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(senha, usuario.getSenha()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");

        String nivelAcesso = isWebLogin ? "ADMIN" : "USUARIO";
        String token = JwtUtil.generateToken(usuario.getEmail());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "usuario", usuario,
            "nivelAcesso", nivelAcesso
        ));
    }

    /** Aceitar convite para equipe */
    @PostMapping("/aceitar-convite")
    public ResponseEntity<?> aceitarConvite(@RequestBody Map<String, String> body) {
        String email = body.get("email"); // email do usuário logado
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
