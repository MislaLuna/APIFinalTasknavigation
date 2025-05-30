package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
}
