package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tasknavigation.demo.domain.Projeto;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.dto.TarefaDTO;
import tasknavigation.demo.service.TarefaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    // Listar todas as tarefas
    @GetMapping
    public List<Tarefa> listar() {
        return tarefaService.listarTodas();
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaService.buscarPorId(id);
        return tarefa.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TarefaDTO dto) {
        Usuario usuario = tarefaService.buscarUsuarioPorId(dto.getIdUsuario());
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo() != null ? dto.getTitulo() : "Título da Tarefa");
        tarefa.setDescricao(dto.getDescricao() != null ? dto.getDescricao() : "Descrição da Tarefa");
        tarefa.setPrazo(dto.getPrazo() != null ? dto.getPrazo() : LocalDate.now().plusDays(7)); // prazo default 7 dias
        tarefa.setStatus(dto.getStatus() != null ? dto.getStatus() : "Pendente");
        tarefa.setPrioridade(dto.getPrioridade() != null ? dto.getPrioridade() : "Média");
        tarefa.setUsuario(usuario);

        if (dto.getIdProjeto() != null) {
            Projeto projeto = tarefaService.buscarProjetoPorId(dto.getIdProjeto());
            if (projeto != null) {
                tarefa.setProjeto(projeto);
            }
        }

        return ResponseEntity.ok(tarefaService.salvar(tarefa));
    }

    // Atualizar tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TarefaDTO dto) {
        Optional<Tarefa> tarefaOpt = tarefaService.buscarPorId(id);
        if (!tarefaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Tarefa tarefa = tarefaOpt.get();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setPrazo(dto.getPrazo());
        tarefa.setStatus(dto.getStatus());
        tarefa.setPrioridade(dto.getPrioridade());
        tarefa.setUsuario(tarefaService.buscarUsuarioPorId(dto.getIdUsuario()));

        if (dto.getIdProjeto() != null) {
            Projeto projeto = tarefaService.buscarProjetoPorId(dto.getIdProjeto());
            tarefa.setProjeto(projeto);
        }

        return ResponseEntity.ok(tarefaService.salvar(tarefa));
    }

    // Deletar tarefa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!tarefaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
