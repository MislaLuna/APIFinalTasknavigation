package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.domain.TarefaParticipante;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.service.TarefaParticipanteService;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/tarefa-participantes")
public class TarefaParticipanteController {

    private final TarefaParticipanteService tarefaParticipanteService;

    @Autowired
    public TarefaParticipanteController(TarefaParticipanteService tarefaParticipanteService) {
        this.tarefaParticipanteService = tarefaParticipanteService;
    }

    // 🔹 Listar todos
    @GetMapping
    public List<TarefaParticipante> listar() {
        return tarefaParticipanteService.listarTodos();
    }

    // 🔹 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<TarefaParticipante> buscarPorId(@PathVariable Long id) {
        try {
            TarefaParticipante tp = tarefaParticipanteService.buscarPorId(id);
            return ResponseEntity.ok(tp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Listar por usuário (para o app mobile)
    @GetMapping("/usuario/{idUsuario}")
    public List<TarefaParticipante> listarPorUsuario(@PathVariable Long idUsuario) {
        return tarefaParticipanteService.listarPorUsuario(idUsuario);
    }

    // 🔹 Criar relação tarefa-usuário
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TarefaParticipante tp) {
        try {
            // Busca as entidades reais
            Tarefa tarefa = tarefaParticipanteService.buscarTarefaPorId(tp.getTarefa().getIdTarefa());
            Usuario usuario = tarefaParticipanteService.buscarUsuarioPorId(tp.getUsuario().getId());

            tp.setTarefa(tarefa);
            tp.setUsuario(usuario);
            tp.setDataVisualizacao(LocalDateTime.now());

            TarefaParticipante salvo = tarefaParticipanteService.salvar(tp);
            return ResponseEntity.ok(salvo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    // 🔹 Atualizar status/comentário
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TarefaParticipante dados) {
        try {
            TarefaParticipante existente = tarefaParticipanteService.buscarPorId(id);
            existente.setStatus(dados.getStatus());
            existente.setComentario(dados.getComentario());
            existente.setDataVisualizacao(LocalDateTime.now());

            TarefaParticipante atualizado = tarefaParticipanteService.salvar(existente);
            return ResponseEntity.ok(atualizado);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Deletar relação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            tarefaParticipanteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
