package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")  // Nome real da coluna no banco
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(name = "data_registro")  // Nome real da coluna no banco
    private LocalDate dataRegistro;

    // Novos campos para confirmação de e-mail
    @Column(name = "email_confirmado")
    private Boolean emailConfirmado = false;

    @Column(name = "token_confirmacao")
    private String tokenConfirmacao;

    @Column(name = "expira_token")
    private LocalDateTime expiraToken;

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String email, String senha, LocalDate dataRegistro) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataRegistro = dataRegistro;
        this.emailConfirmado = false;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Boolean getEmailConfirmado() {
        return emailConfirmado;
    }

    public void setEmailConfirmado(Boolean emailConfirmado) {
        this.emailConfirmado = emailConfirmado;
    }

    public String getTokenConfirmacao() {
        return tokenConfirmacao;
    }

    public void setTokenConfirmacao(String tokenConfirmacao) {
        this.tokenConfirmacao = tokenConfirmacao;
    }

    public LocalDateTime getExpiraToken() {
        return expiraToken;
    }

    public void setExpiraToken(LocalDateTime expiraToken) {
        this.expiraToken = expiraToken;
    }
}
