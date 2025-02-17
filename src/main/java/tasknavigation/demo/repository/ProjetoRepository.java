package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Projeto;

public interface ProjetoRepository extends CrudRepository<Projeto, Long> {
}
