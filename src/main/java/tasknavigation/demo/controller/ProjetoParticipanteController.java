package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.ProjetoParticipante;
import tasknavigation.demo.domain.ProjetoParticipanteId;
import tasknavigation.demo.service.ProjetoParticipanteService;

import java.util.Optional;
@RestController
@RequestMapping("/projetos-participantes")
public class ProjetoParticipanteController {

    @Autowired
    private ProjetoParticipanteService service;

    @GetMapping
    public Iterable<ProjetoParticipante> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{idProjeto}/{idUsuario}")
    public Optional<ProjetoParticipante> buscarPorId(@PathVariable Integer idProjeto, @PathVariable Integer idUsuario) {
        ProjetoParticipanteId id = new ProjetoParticipanteId(idProjeto, idUsuario);
        return service.buscarPorId(id);
    }

    @PostMapping
    public ProjetoParticipante salvar(@RequestBody ProjetoParticipante participante) {
        return service.salvar(participante);
    }

    @DeleteMapping("/{idProjeto}/{idUsuario}")
    public void deletar(@PathVariable Integer idProjeto, @PathVariable Integer idUsuario) {
        ProjetoParticipanteId id = new ProjetoParticipanteId(idProjeto, idUsuario);
        service.deletarPorId(id);
    }
}
