package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Relatorio;

public interface RelatorioRepository extends CrudRepository<Relatorio, Long> {
}
