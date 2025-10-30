package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.TarefaParticipante;

import java.util.List;

public interface TarefaParticipanteRepository extends JpaRepository<TarefaParticipante, Long> {

    List<TarefaParticipante> findByUsuarioId(Long idUsuario);
    List<TarefaParticipante> findByTarefaId(Long idTarefa);
}

