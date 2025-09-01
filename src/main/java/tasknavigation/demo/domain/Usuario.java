package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "nivelAcesso") // mapeia para a coluna no banco
    private String nivelAcesso; // ADMIN ou USUARIO

    // Confirmação de e-mail
    @Column(name = "email_confirmado")
    private Boolean emailConfirmado = false;

    @Column(name = "token_confirmacao")
    private String tokenConfirmacao;

    @Column(name = "expira_token")
    private LocalDateTime expiraToken;

    // Recuperação de senha
    @Column(name = "codigo_recuperacao")
    private String codigoRecuperacao;

    @Column(name = "codigo_expiracao")
    private LocalDateTime codigoExpiracao;

    // Relacionamento com equipe
    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String email, String senha, LocalDate dataRegistro, String nivelAcesso) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataRegistro = dataRegistro;
        this.nivelAcesso = nivelAcesso;
        this.emailConfirmado = false;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDate getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDate dataRegistro) { this.dataRegistro = dataRegistro; }

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }

    public Boolean getEmailConfirmado() { return emailConfirmado; }
    public void setEmailConfirmado(Boolean emailConfirmado) { this.emailConfirmado = emailConfirmado; }

    public String getTokenConfirmacao() { return tokenConfirmacao; }
    public void setTokenConfirmacao(String tokenConfirmacao) { this.tokenConfirmacao = tokenConfirmacao; }

    public LocalDateTime getExpiraToken() { return expiraToken; }
    public void setExpiraToken(LocalDateTime expiraToken) { this.expiraToken = expiraToken; }

    public String getCodigoRecuperacao() { return codigoRecuperacao; }
    public void setCodigoRecuperacao(String codigoRecuperacao) { this.codigoRecuperacao = codigoRecuperacao; }

    public LocalDateTime getCodigoExpiracao() { return codigoExpiracao; }
    public void setCodigoExpiracao(LocalDateTime codigoExpiracao) { this.codigoExpiracao = codigoExpiracao; }

    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
}
