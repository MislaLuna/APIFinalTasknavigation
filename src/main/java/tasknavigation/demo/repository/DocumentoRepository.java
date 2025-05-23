package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Documento;

public interface DocumentoRepository extends CrudRepository<Documento, Integer> {
}
