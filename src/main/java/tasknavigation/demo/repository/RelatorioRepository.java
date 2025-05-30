package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Relatorio;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
}
