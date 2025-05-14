package tasknavigation.demo.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
 
@Entity // Anotação que marca esta classe como uma entidade JPA
public class Usuario {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente no banco
    private Long id_usuario;
 
    private String nome;
 
    @Column(unique = true) // Garante que o e-mail seja único
    private String email;
 
    private String senha;
 
    private LocalDate dataRegistro;
 
    // Construtores, Getters e Setters
    public Usuario() {}
 
    public Usuario(String nome, String email, String senha, LocalDate dataRegistro) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataRegistro = dataRegistro;
    }
 
    public Long getId() {
        return id_usuario;
    }
 
    public void setId(Long id) {
this.id_usuario = id;
    }
 
    public String getNome() {
        return nome;
    }
 
    public void setNome(String nome) {
        this.nome = nome;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
this.email = email;
    }
 
    public String getSenha() {
        return senha;
    }
 
    public void setSenha(String senha) {
        this.senha = senha;
    }
 
    public LocalDate getDataRegistro() {
        return dataRegistro;
    }
 
    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}

