package tasknavigation.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.service.UsuarioService;
import tasknavigation.demo.service.EmailService;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173") // Libera acesso do front-end
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Autowired
    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository, EmailService emailService) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            List<Usuario> usuarios = service.listarUsuario();
            if (usuarios.isEmpty()) {
                return ResponseEntity.ok("Nenhum usuário encontrado.");
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar usuários: " + e.getMessage());
        }
    }

    // Login
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndSenha(email, senha);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar usuário
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario novoUsuario) {
        try {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(novoUsuario.getEmail());
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um usuário com este email.");
            }

            Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(usuarioAtualizado.getSenha());
            Usuario atualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(atualizado);
        }).orElseGet(() -> {
            usuarioAtualizado.setId(id);
            Usuario novoUsuario = usuarioRepository.save(usuarioAtualizado);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        });
    }

    // Recuperação de senha (envia e-mail)
@PostMapping("/recuperar-senha")
public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> body) {
    String email = body.get("email");

    if (email == null || email.isEmpty()) {
        return ResponseEntity.badRequest().body("Email não fornecido.");
    }

    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
    if (usuario.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado com esse email.");
    }

    try {
        String assunto = "Recuperação de Senha - TaskNavigation";
        String corpo = "Olá " + usuario.get().getNome() + ",\n\n" +
                "Recebemos uma solicitação para recuperar sua senha.\n\n" +
                "Caso tenha sido você, clique no link abaixo para redefinir sua senha:\n\n" +
                "[LINK DE RECUPERAÇÃO AQUI]\n\n" +
                "Se não foi você, por favor ignore este e-mail.\n\n" +
                "Atenciosamente,\nEquipe TaskNavigation";

        emailService.enviarEmailSimples(email, assunto, corpo);
        return ResponseEntity.ok("Instruções enviadas para o e-mail fornecido.");
    } catch (Exception e) {
        e.printStackTrace();  // Veja o erro completo no log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao enviar e-mail: " + e.getMessage());
    }
}

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
