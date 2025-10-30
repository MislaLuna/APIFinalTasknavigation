package tasknavigation.demo.dto;

import tasknavigation.demo.domain.TarefaParticipante;
import java.time.LocalDate;

public class TarefaParticipanteDTO {
    private Long idTarefaParticipante;
    private String status;
    private String comentario;
    private String tituloTarefa;
    private LocalDate prazoTarefa;

    public TarefaParticipanteDTO(TarefaParticipante tp) {
        this.idTarefaParticipante = tp.getIdTarefaParticipante();
        this.status = tp.getStatus();
        this.comentario = tp.getComentario();

        // ⚠️ Evita NullPointer se a tarefa estiver nula (segurança extra)
        if (tp.getTarefa() != null) {
            this.tituloTarefa = tp.getTarefa().getTitulo();
            this.prazoTarefa = tp.getTarefa().getPrazo();
        }
    }

    // Getters (necessários para serializar em JSON)
    public Long getIdTarefaParticipante() {
        return idTarefaParticipante;
    }

    public String getStatus() {
        return status;
    }

    public String getComentario() {
        return comentario;
    }

    public String getTituloTarefa() {
        return tituloTarefa;
    }

    public LocalDate getPrazoTarefa() {
        return prazoTarefa;
    }
}
