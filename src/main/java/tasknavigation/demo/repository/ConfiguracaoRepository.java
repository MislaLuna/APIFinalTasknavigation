package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Configuracao;

public interface ConfiguracaoRepository extends CrudRepository<Configuracao, Long> {
}
