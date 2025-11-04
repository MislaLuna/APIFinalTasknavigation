package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    // Busca uma equipe pelo código de convite
    Optional<Equipe> findByCodigoConvite(String codigoConvite);

    

List<Equipe> findByCriadorId(Long idCriador);

    // Busca equipes onde o usuário é membro
    List<Equipe> findByUsuariosId(Long usuarioId);

    // ✅ Verifica se já existe equipe com esse nome
    boolean existsByNome(String nome);
    
}
