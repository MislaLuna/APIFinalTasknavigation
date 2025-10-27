package tasknavigation.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tasknavigation.demo.domain.Configuracao;

import java.util.Optional;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {

    // Como é @OneToOne com 'usuario', o derived query correto usa 'Usuario_Id'
    Optional<Configuracao> findByUsuario_Id(Long usuarioId);

    boolean existsByUsuario_Id(Long usuarioId);
}
