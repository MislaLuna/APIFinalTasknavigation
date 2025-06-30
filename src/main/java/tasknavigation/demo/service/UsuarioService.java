package tasknavigation.demo.service;

import java.util.Optional;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.UsuarioRepository;

import java.sql.CallableStatement;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;

    // Injeção via construtor (melhor prática)
    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, JdbcTemplate jdbcTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
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

    /**
     * Cria usuário via procedure no banco, criptografando a senha aqui,
     * e definindo origem (WEB ou MOBILE)
     */
    public void criarUsuarioViaProcedure(Usuario usuario, String origem) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        String sql = "{call sp_CriarUsuario(?, ?, ?, ?)}";

        jdbcTemplate.update(connection -> {
            CallableStatement cs = connection.prepareCall(sql);
            cs.setString(1, usuario.getNome());
            cs.setString(2, usuario.getEmail());
            cs.setString(3, senhaCriptografada);
            cs.setString(4, origem);
            return cs;
        });
    }
}
