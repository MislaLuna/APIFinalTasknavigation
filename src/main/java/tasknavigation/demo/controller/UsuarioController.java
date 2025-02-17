package tasknavigation.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.service.UsuarioService;
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


//////////////////////////////////////////////////////////////////////

    // Puxa o serviço de usuário para usar as funções dele:
    @Autowired
    private UsuarioService service;
    

//////////////////////////////////////////////////////////////////////

    // Lista todos os usuários(Retorna a lista):
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarioResponseEntity(){
        return new ResponseEntity<>(service.listarUsuario(),HttpStatus.OK);
    }



//////////////////////////////////////////////////////////////////////


    //Importa o repositório de usuários para trabalhar com o banco de dados:
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
///////////////////////////////////////////////////////////////////////



    // Cria um novo usuário e salva no banco de dados:
    @PostMapping
public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
    try {
        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    } catch (Exception e) {
        // Log the error message
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


////////////////////////////////////////////////////////////////////////


     // Busca um usuário pelo ID:
    @GetMapping("/{id}")
    public Optional<Usuario> buscarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id);
    }


/////////////////////////////////////////////////////////////////////////


    // Atualiza um usuário existente pelo ID:
    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id).map(usuario -> {

            // Atualiza os dados do usuário:
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(usuarioAtualizado.getSenha());
            return usuarioRepository.save(usuario);
        }).orElseGet(() -> {

            // Se não achar o usuário, cria um novo com o ID informado:
            usuarioAtualizado.setId(id);
            return usuarioRepository.save(usuarioAtualizado);
        });
    }


///////////////////////////////////////////////////////////////////////////



    // Deleta um usuário pelo ID
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}


