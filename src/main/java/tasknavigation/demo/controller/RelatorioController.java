package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Relatorio;
import tasknavigation.demo.service.RelatorioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public List<Relatorio> listar() {
        return relatorioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relatorio> buscarPorId(@PathVariable Long id) {
        Optional<Relatorio> relatorio = relatorioService.buscarPorId(id);
        return relatorio.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Relatorio criar(@RequestBody Relatorio relatorio) {
        return relatorioService.salvar(relatorio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relatorio> atualizar(@PathVariable Long id, @RequestBody Relatorio relatorio) {
        if (!relatorioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        relatorio.setIdRelatorio(id);
        Relatorio atualizado = relatorioService.salvar(relatorio);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!relatorioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        relatorioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
