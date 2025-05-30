package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Relatorio")
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relatorio")
    private Long idRelatorio;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "dataCriacao")
    private LocalDate dataCriacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Getters e Setters

    public Long getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Long idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
