package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Projeto;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.dto.ProjetoDTO;
import tasknavigation.demo.service.ProjetoService;
import tasknavigation.demo.service.UsuarioService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private UsuarioService usuarioService;

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
    public Projeto criar(@RequestBody ProjetoDTO projetoDTO) {
        if (projetoDTO.getIdUsuario() == null) {
            throw new RuntimeException("O id do usuário é obrigatório.");
        }

        Usuario usuario = usuarioService.obterUsuarioId(projetoDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + projetoDTO.getIdUsuario()));

        Projeto projeto = new Projeto();
        projeto.setNome(projetoDTO.getNome() != null ? projetoDTO.getNome() : "Nome do projeto");
        projeto.setDescricao(projetoDTO.getDescricao() != null ? projetoDTO.getDescricao() : "Descrição do projeto");
        projeto.setPrazo(projetoDTO.getPrazo() != null ? projetoDTO.getPrazo() : LocalDate.now());
        projeto.setUsuario(usuario);

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
