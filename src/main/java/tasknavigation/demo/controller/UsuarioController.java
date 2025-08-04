package tasknavigation.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.service.UsuarioService;
import tasknavigation.demo.service.EmailService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository,
                             EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Map<String, Object> body) {
        try {
            String nome = (String) body.get("nome");
            String email = (String) body.get("email");
            String senha = (String) body.get("senha");
            String origem = (String) body.get("origem");

            if (nome == null || email == null || senha == null || origem == null) {
                return ResponseEntity.badRequest()
                        .body("Todos os campos são obrigatórios (nome, email, senha, origem)");
            }

            if (usuarioRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um usuário com este email.");
            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);

            service.incluirUsuario(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso. Confirme seu e-mail.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    String email = body.get("email");
    String senha = body.get("senha");

    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();

        // 🔒 Verifica se o e-mail já foi confirmado
        if (!usuario.getEmailConfirmado()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Confirme seu e-mail antes de fazer login.");
        }

        // 🔐 Verifica a senha
        boolean valid = passwordEncoder.matches(senha, usuario.getSenha());

        if (valid) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
}


    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<?> confirmarEmail(@RequestParam String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenConfirmacao(token);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Token inválido ou usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getExpiraToken() != null && usuario.getExpiraToken().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Token expirado. Solicite um novo.");
        }

        usuario.setEmailConfirmado(true);
        usuario.setTokenConfirmacao(null);
        usuario.setExpiraToken(null);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("E-mail confirmado com sucesso!");
    }
}
