package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tasknavigation.demo.domain.TarefaParticipante;

import java.util.List;

public interface TarefaParticipanteRepository extends JpaRepository<TarefaParticipante, Long> {

    List<TarefaParticipante> findByUsuarioId(Long idUsuario);

@Query("SELECT tp FROM TarefaParticipante tp WHERE tp.tarefa.idTarefa = :idTarefa")
List<TarefaParticipante> findByTarefaIdTarefa(@Param("idTarefa") Long idTarefa);


}

