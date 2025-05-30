package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Projeto;
import tasknavigation.demo.service.ProjetoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public List<Projeto> listar() {
        return projetoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Long id) {
        Optional<Projeto> projeto = projetoService.buscarPorId(id);
        return projeto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Projeto criar(@RequestBody Projeto projeto) {
        return projetoService.salvar(projeto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projeto> atualizar(@PathVariable Long id, @RequestBody Projeto projeto) {
        if (!projetoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projeto.setIdProjeto(id);
        Projeto atualizado = projetoService.salvar(projeto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!projetoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projetoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
