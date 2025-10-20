package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Equipe;

import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    // Busca uma equipe pelo código de convite
    Optional<Equipe> findByCodigoConvite(String codigoConvite);

    // ✅ Verifica se já existe equipe com esse nome
    boolean existsByNome(String nome);
}
