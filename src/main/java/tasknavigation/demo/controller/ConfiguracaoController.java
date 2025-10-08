package tasknavigation.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.ConfiguracaoRepository;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.service.ConfiguracaoService;

@RestController
@RequestMapping("/configuracao")
public class ConfiguracaoController {

    private final ConfiguracaoRepository configuracaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private ConfiguracaoService service;

    @Autowired
    public ConfiguracaoController(ConfiguracaoRepository configuracaoRepository, UsuarioRepository usuarioRepository) {
        this.configuracaoRepository = configuracaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Configuracao>> listarConfiguracoes() {
        return ResponseEntity.ok(service.listarConfiguracao());
    }

    @PostMapping
    public ResponseEntity<Configuracao> criarConfiguracao(@RequestBody Configuracao configuracao) {
        try {
            Configuracao novaConfiguracao = configuracaoRepository.save(configuracao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConfiguracao);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Configuracao> buscarConfiguracao(@PathVariable Long id) {
        return configuracaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Configuracao> atualizarConfiguracao(
            @PathVariable Long id,
            @RequestBody Configuracao configuracaoAtualizado) {

        return configuracaoRepository.findById(id).map(configuracao -> {
            configuracao.setFotoPerfil(configuracaoAtualizado.getFotoPerfil());
            configuracao.setNotificacoes(configuracaoAtualizado.getNotificacoes());
            configuracao.setTema(configuracaoAtualizado.getTema());

            if (configuracaoAtualizado.getUsuario() != null &&
                configuracaoAtualizado.getUsuario().getId() != null) {
                Usuario usuario = usuarioRepository.findById(configuracaoAtualizado.getUsuario().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
                configuracao.setUsuario(usuario);
            }

            return ResponseEntity.ok(configuracaoRepository.save(configuracao));
        }).orElseGet(() -> {
            if (configuracaoAtualizado.getUsuario() != null &&
                configuracaoAtualizado.getUsuario().getId() != null) {
                Usuario usuario = usuarioRepository.findById(configuracaoAtualizado.getUsuario().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
                configuracaoAtualizado.setUsuario(usuario);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(configuracaoRepository.save(configuracaoAtualizado));
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConfiguracao(@PathVariable Long id) {
        if (configuracaoRepository.existsById(id)) {
            configuracaoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
