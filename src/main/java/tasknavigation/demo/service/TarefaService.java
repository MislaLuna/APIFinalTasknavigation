package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
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
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarUsuarioPorId(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("O id do usuário não pode ser nulo");
        }
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }

    public Projeto buscarProjetoPorId(Long idProjeto) {
        return projetoRepository.findById(idProjeto).orElse(null);
    }

    // NOVO: listar todas as tarefas
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }
}
8v
