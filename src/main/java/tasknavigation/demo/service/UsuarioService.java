package tasknavigation.demo.service;
 
import java.util.Optional;
 import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository UsuarioRepository;
 
    public List<Usuario> listarUsuario(){
        return (List<Usuario>) UsuarioRepository.findAll();
 
 
 
 
 //Buscar um produto pelo Id
      
    }
 
    public Optional<Usuario> obterUsuarioId(Long id) {
        return UsuarioRepository.findById(id);
    }
 




    //Incluir novo usuario
 
    public Usuario incluir(Usuario usuario){
        return UsuarioRepository.save(usuario);
    }
 
    public Usuario incluirUsuario(Usuario usuario) {
        return UsuarioRepository.save(usuario);
       
    }
 
    



 
    //atualizar usuario
    public Usuario atualizaUsuario(Long id, Usuario usuario){




    
        //verificar se o produto existe
        if (UsuarioRepository.existsById(id)){
            usuario.setId(id);
            return UsuarioRepository.save(usuario);
        } else {
            return null;
        }
    }
}
