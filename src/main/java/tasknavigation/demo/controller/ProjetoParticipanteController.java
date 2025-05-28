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
public Optional<ProjetoParticipante> buscarPorId(@PathVariable Long idProjeto, @PathVariable Long idUsuario) {
    ProjetoParticipanteId id = new ProjetoParticipanteId(idProjeto, idUsuario);
    return service.buscarPorId(id);
}

@DeleteMapping("/{idProjeto}/{idUsuario}")
public void deletar(@PathVariable Long idProjeto, @PathVariable Long idUsuario) {
    ProjetoParticipanteId id = new ProjetoParticipanteId(idProjeto, idUsuario);
    service.deletarPorId(id);
}

}
