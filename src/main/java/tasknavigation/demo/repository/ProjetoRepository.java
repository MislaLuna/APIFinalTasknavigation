package tasknavigation.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
