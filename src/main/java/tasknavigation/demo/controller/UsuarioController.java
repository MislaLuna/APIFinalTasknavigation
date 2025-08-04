package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Outras rotas do controller podem ficar aqui ---

    // Atualizar senha após recuperação - sem validar código (pois front não envia)
    @PostMapping("/atualizar-senha")
    public ResponseEntity<String> atualizarSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String novaSenha = body.get("novaSenha");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoExpiracao(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    // Você pode adicionar as outras rotas que já tem, ex login, cadastro, recuperar senha, etc.

}
