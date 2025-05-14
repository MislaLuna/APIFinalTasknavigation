package tasknavigation.demo.domain;

import jakarta.persistence.*;

@Entity
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracao") // Mapeia a coluna id_configuracao
    private Long idConfiguracao;

    @Column(name = "notificacoes") // Mapeia a coluna notificacoes
    private Boolean notificacoes;

    @Column(name = "foto_perfil") // Mapeia a coluna foto_perfil
    private String fotoPerfil;

    @OneToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "usuario_id") // Mapeia a coluna usuario_id
    private Usuario usuario;

    // Getters e Setters
    public Long getIdConfiguracao() {
        return idConfiguracao;
    }

    public void setIdConfiguracao(Long idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    public Boolean getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(Boolean notificacoes) {
        this.notificacoes = notificacoes;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
