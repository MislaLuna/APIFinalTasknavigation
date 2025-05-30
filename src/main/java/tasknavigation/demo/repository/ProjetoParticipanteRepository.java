package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.ProjetoParticipante;
import tasknavigation.demo.domain.ProjetoParticipanteId;

public interface ProjetoParticipanteRepository extends CrudRepository<ProjetoParticipante, ProjetoParticipanteId> {
}
