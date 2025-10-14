package tasknavigation.demo.service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

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
    private final EmailService emailService;

    public UsuarioService(
        PasswordEncoder passwordEncoder,
        UsuarioRepository usuarioRepository,
        JdbcTemplate jdbcTemplate,
        EmailService emailService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
    }

    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterUsuarioId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ✅ Novo método: busca usuários ativos por equipe
    public List<Usuario> buscarPorEquipe(Long equipeId) {
        return usuarioRepository.findByEquipeIdAndCodStatusTrue(equipeId);
    }

    /*
    public Usuario incluirUsuario(Usuario usuario) { ... }
    public Usuario atualizaUsuario(Long id, Usuario usuario) { ... }
    public void criarUsuarioViaProcedure(Usuario usuario, String origem) { ... }
    public boolean confirmarEmail(String token) { ... }
    private String gerarToken() { ... }
    */
}
