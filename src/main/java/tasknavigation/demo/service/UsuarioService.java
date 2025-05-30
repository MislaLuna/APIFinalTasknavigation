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
    private UsuarioRepository usuarioRepository;

    // Lista todos os usuários
    public List<Usuario> listarUsuario() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    // Buscar um usuário pelo ID
    public Optional<Usuario> obterUsuarioId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Incluir novo usuário
    public Usuario incluirUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Atualizar usuário existente
    public Usuario atualizaUsuario(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        } else {
            return null;
        }
    }
}
