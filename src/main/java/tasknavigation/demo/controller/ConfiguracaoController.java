package tasknavigation.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.service.ConfiguracaoService;

@RestController
@RequestMapping("/configuracao")
public class ConfiguracaoController {

    private final ConfiguracaoService service;

    public ConfiguracaoController(ConfiguracaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Configuracao>> listarConfiguracoes() {
        return ResponseEntity.ok(service.listarConfiguracao());
    }

    /**
     * Busca a configuração pelo ID do usuário. Se não existir, cria default e retorna 200.
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Configuracao> buscarPorUsuario(@PathVariable Long usuarioId) {
        Configuracao cfg = service.getOrCreateByUsuarioId(usuarioId);
        return ResponseEntity.ok(cfg);
    }

    /**
     * Upsert: atualiza se existir, cria se não existir, recebendo JSON.
     * Body esperado (exemplo):
     * {
     *   "tema": "escuro",
     *   "notificacoes": true,
     *   "fotoPerfil": "data:image/jpeg;base64,...",
     *   "posicaoFoto": 2
     * }
     */
    @PutMapping("/{usuarioId}")
    public ResponseEntity<Configuracao> atualizarPorUsuario(@PathVariable Long usuarioId,
                                                            @RequestBody Configuracao body) {
        Configuracao salvo = service.upsertByUsuarioId(usuarioId, body);
        return ResponseEntity.ok(salvo);
    }

    // (opcional) deletar por id da própria configuração
    @DeleteMapping("/id/{idConfiguracao}")
    public ResponseEntity<Void> deletarConfiguracao(@PathVariable Long idConfiguracao) {
        service.deletar(idConfiguracao);
        return ResponseEntity.noContent().build();
    }
}
