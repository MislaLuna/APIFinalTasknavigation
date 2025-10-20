package tasknavigation.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndSenha(String email, String senha);

    Optional<Usuario> findByTokenConfirmacao(String tokenConfirmacao);

    // Novo método para buscar usuários ativos por equipe
    List<Usuario> findByEquipeIdAndCodStatusTrue(Long equipeId);

    Optional<Usuario> findByCodigoRecuperacao(String codigoRecuperacao);

}
