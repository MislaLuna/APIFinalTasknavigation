// Documento.java
package tasknavigation.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Documento")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "nomeArquivo")
    private String nomeArquivo;

    @Column(name = "caminhoArquivo")
    private String caminhoArquivo;
    
    @Column(name = "dataUpload")
    private LocalDate dataUpload;
    
    @ManyToOne
    @JoinColumn(name = "id_projeto")
    @JsonIgnoreProperties("documentos")
    private Projeto projeto;

    // Getters e Setters
    public Integer getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Integer idDocumento) { this.idDocumento = idDocumento; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }

    public LocalDate getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDate dataUpload) { this.dataUpload = dataUpload; }

    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
}
