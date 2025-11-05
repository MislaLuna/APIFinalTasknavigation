package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.auth.AuthenticationRequest;
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
        "http://localhost:5500",
        "http://localhost:5173",
        "http://127.0.0.1:5500",
        "http://192.168.101.12:5500"
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

    /** DTO seguro para retorno de usuário */
    public static class UsuarioResponse {
        public Long id;
        public String nome;
        public String email;
        public NivelAcesso nivelAcesso;

        public UsuarioResponse(Usuario usuario) {
            this.id = usuario.getId();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.nivelAcesso = usuario.getNivelAcesso();
        }
    }

    /** Listar todos os usuários */
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuario();
        List<UsuarioResponse> response = new ArrayList<>();
        for (Usuario u : usuarios) response.add(new UsuarioResponse(u));
        return ResponseEntity.ok(response);
    }

    /** Criar nova conta */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        String email = (String) body.get("email");
        String senha = (String) body.get("senha");
        String nivel = (String) body.getOrDefault("nivel", "USUARIO");

        if (usuarioService.buscarPorEmail(email).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "E-mail já cadastrado."));

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setNivelAcesso("ADMIN".equalsIgnoreCase(nivel) ? NivelAcesso.ADMIN : NivelAcesso.USUARIO);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(usuario.getEmail());
        registerRequest.setPassword(usuario.getSenha());
        registerRequest.setNome(usuario.getNome());
        registerRequest.setNivelAcesso(usuario.getNivelAcesso());

        Usuario novoUsuario = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponse(novoUsuario));
    }

    /** Enviar código de recuperação de senha */
    @PostMapping("/enviar-codigo-recuperacao")
    public ResponseEntity<?> enviarCodigoRecuperacao(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if (email == null || email.isBlank())
                return ResponseEntity.badRequest().body(Map.of("success", false, "mensagem", "E-mail não fornecido."));

            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
            if (usuarioOpt.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "mensagem", "Usuário não encontrado."));

            Usuario usuario = usuarioOpt.get();
            String codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            usuario.setCodigoRecuperacao(codigo);
            usuario.setCodigoExpiracao(LocalDateTime.now().plusMinutes(15));
            usuarioService.salvar(usuario);

            emailService.enviarCodigoRecuperacao(usuario.getEmail(), codigo);
            return ResponseEntity.ok(Map.of("success", true, "mensagem", "Código enviado para o e-mail."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "mensagem", "Erro interno ao enviar código."));
        }
    }

    /** Verificar código de recuperação */
    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Usuário não encontrado."));

        Usuario usuario = usuarioOpt.get();
       if (codigo == null || codigo.isBlank()) {
    return ResponseEntity.badRequest().body(Map.of("erro", "Código não informado."));
}

if (usuario.getCodigoRecuperacao() == null) {
    return ResponseEntity.badRequest().body(Map.of("erro", "Nenhum código de recuperação foi gerado para este usuário."));
}

if (!codigo.equalsIgnoreCase(usuario.getCodigoRecuperacao())) {
    return ResponseEntity.badRequest().body(Map.of("erro", "Código inválido."));
}

if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
    return ResponseEntity.badRequest().body(Map.of("erro", "Código expirado."));
}

        return ResponseEntity.ok(Map.of("success", true, "mensagem", "Código válido."));
    }

    /** Redefinir senha */
   /** Redefinir senha */
@PostMapping("/recuperar-senha")
public ResponseEntity<?> recuperarSenha(@RequestBody Map<String, String> body) {
    String email = body.get("email");
    String codigo = body.get("codigo");
    String novaSenha = body.get("novaSenha");

    Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
    if (usuarioOpt.isEmpty())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Usuário não encontrado."));

    Usuario usuario = usuarioOpt.get();

    if (codigo == null || codigo.isBlank()) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Código não informado."));
    }

    if (usuario.getCodigoRecuperacao() == null) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Nenhum código de recuperação foi gerado para este usuário."));
    }

    if (!codigo.equalsIgnoreCase(usuario.getCodigoRecuperacao())) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Código inválido."));
    }

    if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Código expirado."));
    }

    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuario.setCodigoRecuperacao(null);
    usuario.setCodigoExpiracao(null);
    usuarioService.salvar(usuario);

    return ResponseEntity.ok(Map.of("success", true, "mensagem", "Senha redefinida com sucesso!"));
}


    /** Listar usuários por equipe */
    @GetMapping("/equipe/{equipeId}")
    public ResponseEntity<?> listarUsuariosPorEquipe(@PathVariable Long equipeId) {
        List<Usuario> usuarios = usuarioService.buscarPorEquipe(equipeId);
        List<UsuarioResponse> response = new ArrayList<>();
        for (Usuario u : usuarios) response.add(new UsuarioResponse(u));
        return ResponseEntity.ok(response);
    }

    /** Buscar usuário pelo ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Usuário não encontrado."));
        return ResponseEntity.ok(new UsuarioResponse(usuarioOpt.get()));
    }

    /** Aceitar convite para equipe */
    @PostMapping("/aceitar-convite")
    public ResponseEntity<?> aceitarConvite(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigoConvite = body.get("codigoConvite");

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Usuário não encontrado."));

        Usuario usuario = usuarioOpt.get();
        Optional<Equipe> equipeOpt = equipeService.buscarPorCodigoConvite(codigoConvite);
        if (equipeOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Código de convite inválido."));

        usuario.setEquipe(equipeOpt.get());
        usuarioService.salvar(usuario);

        return ResponseEntity.ok(Map.of("success", true, "mensagem", "Convite aceito com sucesso!"));
    }
}
