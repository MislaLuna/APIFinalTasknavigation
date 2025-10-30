package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TarefaParticipante")
public class TarefaParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa_participante")
    private Long idTarefaParticipante;

    @ManyToOne
    @JoinColumn(name = "id_tarefa", nullable = false)
    @JsonIgnoreProperties({"usuario", "projeto"})
    private Tarefa tarefa;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"tarefas", "projetos"})
    private Usuario usuario;

    private String status; // "Pendente", "Em andamento", "Concluída"

    @Column(name = "data_visualizacao") // <- Corrigido aqui
    private LocalDateTime dataVisualizacao;

    private String comentario;

    // Construtor padrão
    public TarefaParticipante() {}

    // Getters e setters
    public Long getIdTarefaParticipante() {
        return idTarefaParticipante;
    }

    public void setIdTarefaParticipante(Long idTarefaParticipante) {
        this.idTarefaParticipante = idTarefaParticipante;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(LocalDateTime dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
