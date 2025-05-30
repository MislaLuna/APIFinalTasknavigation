package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.repository.TarefaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> buscarPorId(Integer id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Integer id) {
        tarefaRepository.deleteById(id);
    }
}
