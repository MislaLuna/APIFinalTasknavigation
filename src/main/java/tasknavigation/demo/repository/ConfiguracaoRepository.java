package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
    Configuracao findByUsuarioId(Long usuarioId);
}
