    package tasknavigation.demo.service;

    import java.util.Optional;
    import java.util.List;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import tasknavigation.demo.domain.Usuario;
    import tasknavigation.demo.repository.UsuarioRepository;

    @Service
public class UsuarioService {

    
    private PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }
    

    public List<Usuario> listarUsuario() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    public Optional<Usuario> obterUsuarioId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario incluirUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizaUsuario(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        } else {
            return null;
        }
    }
}
