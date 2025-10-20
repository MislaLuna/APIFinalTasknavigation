package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import tasknavigation.demo.domain.Usuario;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    /** Envia e-mail simples */
    public void enviarEmailSimples(String to, String subject, String text) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(to);
        mensagem.setSubject(subject);
        mensagem.setText(text);
        mailSender.send(mensagem);
    }

    /** Envia confirmação de e-mail */
    public void enviarConfirmacaoEmail(String emailDestino, String token) {
        String linkConfirmacao = baseUrl + "/usuarios/confirmar-email?token=" + token;

        String assunto = "Confirmação de E-mail - TaskNavigation";
        String corpo = "Olá! Obrigado por se cadastrar no TaskNavigation.\n\n"
                    + "Por favor, clique no link abaixo para confirmar seu e-mail:\n"
                    + linkConfirmacao + "\n\n"
                    + "Este link expira em 30 minutos.";

        enviarEmailSimples(emailDestino, assunto, corpo);
    }

    /** Envia convite para colaborador de equipe */
    public void enviarConviteColaborador(String emailDestino, String nomeEquipe, String codigoConvite) {
        String assunto = "Convite para Equipe - TaskNavigation";
        String corpo = "Olá!\n\nVocê foi convidado para a equipe \"" + nomeEquipe + "\".\n" +
                       "Código de convite: " + codigoConvite + "\n\n" +
                       "Acesse o TaskNavigation para aceitar o convite.";

        enviarEmailSimples(emailDestino, assunto, corpo);
    }

    /** Envia código de recuperação de senha */
    public void enviarCodigoRecuperacao(String emailDestino, String codigo) {
        String assunto = "Recuperação de Senha - TaskNavigation";
        String corpo = "Olá!\n\nSeu código para recuperação de senha é: " + codigo + "\n\n"
                    + "Use este código para redefinir sua senha. Ele é válido por 15 minutos.\n"
                    + "Se você não solicitou essa recuperação, ignore este e-mail.";

        enviarEmailSimples(emailDestino, assunto, corpo);
    }

    public void enviarEmailConfirmacao(Usuario usuarioSalvo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enviarEmailConfirmacao'");
    }
}
