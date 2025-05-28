package tasknavigation.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProjetoParticipanteId implements Serializable {

    @Column(name = "id_projeto")
    private Long idProjeto;

    @Column(name = "id_usuario")
    private Long idUsuario;

    public ProjetoParticipanteId() {
    }

    public ProjetoParticipanteId(Long idProjeto, Long idUsuario) {
        this.idProjeto = idProjeto;
        this.idUsuario = idUsuario;
    }

    public Long getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjetoParticipanteId)) return false;
        ProjetoParticipanteId that = (ProjetoParticipanteId) o;
        return Objects.equals(getIdProjeto(), that.getIdProjeto()) &&
               Objects.equals(getIdUsuario(), that.getIdUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdProjeto(), getIdUsuario());
    }
}
