package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Documento;
import tasknavigation.demo.service.DocumentoService;

import java.util.Optional;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public Iterable<Documento> listarTodos() {
        return documentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Documento> buscarPorId(@PathVariable Integer id) {
        return documentoService.buscarPorId(id);
    }

    @PostMapping
    public Documento salvar(@RequestBody Documento documento) {
        return documentoService.salvar(documento);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        documentoService.deletarPorId(id);
    }
}
