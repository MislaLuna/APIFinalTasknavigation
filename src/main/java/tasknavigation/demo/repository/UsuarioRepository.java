package tasknavigation.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Usuario;
 
public interface UsuarioRepository extends CrudRepository<Usuario,Long>{

    Optional<Usuario> findByEmailAndSenha(String email, String senha);

   
}
 