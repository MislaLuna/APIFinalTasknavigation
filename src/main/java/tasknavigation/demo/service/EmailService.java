package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public void enviarEmailSimples(String to, String subject, String text) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(to);
        mensagem.setSubject(subject);
        mensagem.setText(text);
        mailSender.send(mensagem);
    }

    public void enviarConfirmacaoEmail(String emailDestino, String token) {
        String linkConfirmacao = baseUrl + "/usuarios/confirmar-email?token=" + token;

        String assunto = "Confirmação de E-mail - TaskNavigation";
        String corpo = "Olá! Obrigado por se cadastrar no TaskNavigation.\n\n"
                     + "Por favor, clique no link abaixo para confirmar seu e-mail:\n"
                     + linkConfirmacao + "\n\n"
                     + "Este link expira em 30 minutos.";

        enviarEmailSimples(emailDestino, assunto, corpo);
    }

    // Novo método para envio do código de recuperação de senha
    public void enviarCodigoRecuperacao(String emailDestino, String codigo) {
        String assunto = "Recuperação de Senha - TaskNavigation";
        String corpo = "Olá!\n\nSeu código para recuperação de senha é: " + codigo + "\n\n"
                     + "Use este código para redefinir sua senha. Ele é válido por 15 minutos.\n"
                     + "Se você não solicitou essa recuperação, ignore este e-mail.";

        enviarEmailSimples(emailDestino, assunto, corpo);
    }
}
