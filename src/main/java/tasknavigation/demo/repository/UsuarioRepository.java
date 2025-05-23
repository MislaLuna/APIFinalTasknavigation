package tasknavigation.demo.repository;

import org.springframework.data.repository.CrudRepository;
import tasknavigation.demo.domain.Usuario;
 
public interface UsuarioRepository extends CrudRepository<Usuario,Long>{
   
}
 