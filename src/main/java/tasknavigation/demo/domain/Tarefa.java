package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Long idTarefa;

    private String titulo;
    private String descricao;
    private LocalDate prazo;

    @Column(nullable = false)
    private String status; // "Pendente" ou "Concluída"

    @Column(nullable = false)
    private String prioridade; // "Alta", "Média", "Baixa"
    
@ManyToOne
@JoinColumn(name = "id_projeto", nullable = true) // <- aqui permitimos nulo
@JsonIgnoreProperties("tarefas")
private Projeto projeto;

@ManyToOne
@JoinColumn(name = "id_usuario")
@JsonIgnoreProperties({"projetos", "tarefas"}) // evita loop com o usuário
private Usuario usuario;




    public Tarefa() {}

    // Getters e setters

    public Long getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Long id) {
        this.idTarefa = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
}
