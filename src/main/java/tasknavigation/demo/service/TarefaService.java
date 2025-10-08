package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.domain.Projeto;
import tasknavigation.demo.repository.TarefaRepository;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.repository.ProjetoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ProjetoRepository projetoRepository; // injetar repositório de projeto

@Autowired
private UsuarioRepository usuarioRepository;

public Usuario buscarUsuarioPorId(Long id) {
    return usuarioRepository.findById(id).orElse(null);
}

    // Listar todas as tarefas
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    // Buscar tarefa por ID
    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    // Salvar ou atualizar tarefa
    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    // Deletar tarefa
    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }

    // Buscar projeto pelo ID
    public Projeto buscarProjetoPorId(Long idProjeto) {
        return projetoRepository.findById(idProjeto).orElse(null);
    }
}
