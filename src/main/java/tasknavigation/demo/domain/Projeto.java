package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;
    public String descricao;
    public LocalDate prazo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    public Usuario usuario;

    // Getters e Setters
}
