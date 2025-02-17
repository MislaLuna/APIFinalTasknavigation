package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String conteudo;
    public LocalDate dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Getters e Setters
}
