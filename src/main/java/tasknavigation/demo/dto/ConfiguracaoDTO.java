// src/main/java/tasknavigation/demo/dto/ConfiguracaoDTO.java
package tasknavigation.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfiguracaoDTO {
    private Long usuarioId;
    private String tema;
    private boolean notificacoesEmail;
    private String linguagem;
}
