package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.service.TarefaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<Tarefa> listar() {
        return tarefaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Integer id) {
        Optional<Tarefa> tarefa = tarefaService.buscarPorId(id);
        return tarefa.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tarefa criar(@RequestBody Tarefa tarefa) {
        return tarefaService.salvar(tarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Integer id, @RequestBody Tarefa tarefa) {
        if (!tarefaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tarefa.setIdTarefa(id);
        return ResponseEntity.ok(tarefaService.salvar(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!tarefaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
