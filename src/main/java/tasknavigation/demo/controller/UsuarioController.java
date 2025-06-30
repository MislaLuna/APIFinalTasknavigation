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

    // Criar usuário — envia senha limpa para o service que faz a criptografia
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
            usuario.setSenha(senha);  // senha sem criptografia aqui

            System.out.println("🔐 Criando usuário...");
            System.out.println("Senha recebida: " + senha);

            service.criarUsuarioViaProcedure(usuario, origem);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    // Login do usuário — compara senha com hash salvo no banco
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            System.out.println("🔐 Tentando login com:");
            System.out.println("Email: '" + email + "'");
            System.out.println("Senha recebida: '" + senha + "'");
            System.out.println("Senha no banco: '" + usuario.getSenha() + "'");

            boolean valid = passwordEncoder.matches(senha, usuario.getSenha());
            System.out.println("Senha válida? " + valid);

            if (valid) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }
}
