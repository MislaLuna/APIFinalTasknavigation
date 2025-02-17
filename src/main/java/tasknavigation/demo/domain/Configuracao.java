package tasknavigation.demo.domain;

import jakarta.persistence.*;

@Entity
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Boolean notificacoes;
    private String fotoPerfil;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
