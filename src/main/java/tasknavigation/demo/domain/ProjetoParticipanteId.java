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

    public ProjetoParticipanteId() {}

    public ProjetoParticipanteId(Long idProjeto, Long idUsuario) {
        this.idProjeto = idProjeto;
        this.idUsuario = idUsuario;
    }

    // getters e setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjetoParticipanteId)) return false;
        ProjetoParticipanteId that = (ProjetoParticipanteId) o;
        return Objects.equals(idProjeto, that.idProjeto) &&
               Objects.equals(idUsuario, that.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProjeto, idUsuario);
    }
}
