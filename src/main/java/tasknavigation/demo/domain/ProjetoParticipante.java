package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Projeto_Participante")
public class ProjetoParticipante implements Serializable {

    @EmbeddedId
    private ProjetoParticipanteId id;

    @MapsId("idProjeto")
    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @MapsId("idUsuario")
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private Integer progresso;

    // Construtor padrão
    public ProjetoParticipante() {}

    // Getters e Setters

    public ProjetoParticipanteId getId() {
        return id;
    }

    public void setId(ProjetoParticipanteId id) {
        this.id = id;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getProgresso() {
        return progresso;
    }

    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }
}
