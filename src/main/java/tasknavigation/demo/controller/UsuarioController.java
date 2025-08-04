package tasknavigation.demo.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Enviar código de recuperação por e-mail
    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        String codigo = gerarCodigo();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(10);

        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoExpiracao(expiracao);
        usuarioRepository.save(usuario);

        try {
            enviarEmailCodigo(usuario.getEmail(), usuario.getNome(), codigo);
            return ResponseEntity.ok("Código de recuperação enviado para o e-mail.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar e-mail.");
        }
    }

    // ✅ Redefinir senha com código
    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");
        String novaSenha = body.get("novaSenha");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getCodigoRecuperacao() == null || !usuario.getCodigoRecuperacao().equals(codigo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido.");
        }

        if (usuario.getCodigoExpiracao() == null || usuario.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoExpiracao(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    // Utilitários
    private String gerarCodigo() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // 6 dígitos
        return String.valueOf(codigo);
    }

    private void enviarEmailCodigo(String email, String nome, String codigo) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Recuperação de Senha - Task Navigation");

        String conteudo = "<h3>Olá, " + nome + "</h3>"
                + "<p>Use o código abaixo para redefinir sua senha:</p>"
                + "<h2 style='color:blue;'>" + codigo + "</h2>"
                + "<p>O código expira em 10 minutos.</p>";

        helper.setText(conteudo, true);
        javaMailSender.send(message);
    }
}
