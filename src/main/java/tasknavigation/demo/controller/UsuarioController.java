package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.service.EmailService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // ✅ Injeção do serviço de e-mail

    /**
     * Etapa 1 - Envia o código de recuperação para o e-mail informado
     */
    @PostMapping("/enviar-codigo-recuperacao")
    public ResponseEntity<String> enviarCodigoRecuperacao(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        String codigo = UUID.randomUUID().toString().substring(0, 6); // código de 6 caracteres
        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoExpiracao(LocalDateTime.now().plusMinutes(15)); // expira em 15 min
        usuarioRepository.save(usuario);

        // Envia o e-mail usando o EmailService
        emailService.enviarCodigoRecuperacao(usuario.getEmail(), codigo);

        return ResponseEntity.ok("Código enviado para o e-mail.");
    }

    /**
     * Etapa 2 - Verifica se o código informado é válido e não expirou
     */
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!codigo.equals(usuario.getCodigoRecuperacao())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido.");
        }

        if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código expirado.");
        }

        return ResponseEntity.ok("Código válido.");
    }

    /**
     * Etapa 3 - Redefine a senha após o código ser validado
     */
    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String novaSenha = body.get("novaSenha");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setCodigoRecuperacao(null); // limpa o código após uso
        usuario.setCodigoExpiracao(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }
        // *** NOVO ENDPOINT: LOGIN ***
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
            String email = body.get("email");
            String senha = body.get("senha");
    
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
            }
    
            Usuario usuario = usuarioOpt.get();
    
            if (!passwordEncoder.matches(senha, usuario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
            }
    
            // Retorna os dados do usuário, ou pode retornar token JWT se quiser
            return ResponseEntity.ok(usuario);
        }
}
