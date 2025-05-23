package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.ProjetoParticipante;
import tasknavigation.demo.domain.ProjetoParticipanteId;
import tasknavigation.demo.repository.ProjetoParticipanteRepository;

import java.util.Optional;

@Service
public class ProjetoParticipanteService {

    @Autowired
    private ProjetoParticipanteRepository repository;

    public Iterable<ProjetoParticipante> listarTodos() {
        return repository.findAll();
    }

    public Optional<ProjetoParticipante> buscarPorId(ProjetoParticipanteId id) {
        return repository.findById(id);
    }

    public ProjetoParticipante salvar(ProjetoParticipante projetoParticipante) {
        return repository.save(projetoParticipante);
    }

    public void deletarPorId(ProjetoParticipanteId id) {
        repository.deleteById(id);
    }
}
