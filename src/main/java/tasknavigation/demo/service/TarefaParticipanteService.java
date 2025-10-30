package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import tasknavigation.demo.domain.Tarefa;
import tasknavigation.demo.domain.TarefaParticipante;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.TarefaParticipanteRepository;
import tasknavigation.demo.repository.TarefaRepository;
import tasknavigation.demo.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaParticipanteService {

    private final TarefaParticipanteRepository tarefaParticipanteRepository;
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TarefaParticipanteService(
            TarefaParticipanteRepository tarefaParticipanteRepository,
            TarefaRepository tarefaRepository,
            UsuarioRepository usuarioRepository) {
        this.tarefaParticipanteRepository = tarefaParticipanteRepository;
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<TarefaParticipante> listarTodos() {
        return tarefaParticipanteRepository.findAll();
    }

    public TarefaParticipante buscarPorId(Long id) {
        return tarefaParticipanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TarefaParticipante não encontrada com ID: " + id));
    }

    public List<TarefaParticipante> listarPorUsuario(Long idUsuario) {
        // Certifique-se de que o repositório possui o método correto
        return tarefaParticipanteRepository.findByUsuarioId(idUsuario);
    }

    public List<TarefaParticipante> listarPorTarefa(Long idTarefa) {
        return tarefaParticipanteRepository.findByTarefaId(idTarefa);
    }

    public TarefaParticipante salvar(TarefaParticipante tarefaParticipante) {
        if (tarefaParticipante.getTarefa() == null || tarefaParticipante.getUsuario() == null) {
            throw new IllegalArgumentException("Tarefa e Usuário são obrigatórios.");
        }

        if (tarefaParticipante.getDataVisualizacao() == null) {
            tarefaParticipante.setDataVisualizacao(LocalDateTime.now());
        }

        return tarefaParticipanteRepository.save(tarefaParticipante);
    }

    public void deletar(Long id) {
        if (!tarefaParticipanteRepository.existsById(id)) {
            throw new EntityNotFoundException("TarefaParticipante não encontrada com ID: " + id);
        }
        tarefaParticipanteRepository.deleteById(id);
    }

    public Tarefa buscarTarefaPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }
}
