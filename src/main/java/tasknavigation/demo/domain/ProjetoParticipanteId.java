package tasknavigation.demo.domain;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class ProjetoParticipanteId implements Serializable {

    @Column(name = "id_projeto")
    private Integer idProjeto;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public ProjetoParticipanteId() {}

    public ProjetoParticipanteId(Integer idProjeto, Integer idUsuario) {
        this.idProjeto = idProjeto;
        this.idUsuario = idUsuario;
    }

    public Integer getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Integer idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjetoParticipanteId)) return false;
        ProjetoParticipanteId that = (ProjetoParticipanteId) o;
        return Objects.equals(idProjeto, that.idProjeto) && Objects.equals(idUsuario, that.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProjeto, idUsuario);
    }
}
