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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Lista todos os usuários com verificação
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

    // Cria um novo usuário
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");
    
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndSenha(email, senha);
    
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }
    }

    // Busca um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualiza um usuário existente pelo ID
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

    // Deleta um usuário pelo ID
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
