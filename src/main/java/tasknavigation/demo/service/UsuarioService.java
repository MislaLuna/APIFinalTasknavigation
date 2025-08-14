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

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario incluirUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuario.setEmailConfirmado(false);
        usuario.setTokenConfirmacao(gerarToken());
        usuario.setExpiraToken(LocalDateTime.now().plusMinutes(30));

        Usuario salvo = usuarioRepository.save(usuario);
        emailService.enviarConfirmacaoEmail(usuario.getEmail(), salvo.getTokenConfirmacao());

        return salvo;
    }

    public Usuario atualizaUsuario(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        } else {
            return null;
        }
    }

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

    public boolean confirmarEmail(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenConfirmacao(token);

        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getExpiraToken() == null || usuario.getExpiraToken().isBefore(LocalDateTime.now())) {
            return false;
        }

        usuario.setEmailConfirmado(true);
        usuario.setTokenConfirmacao(null);
        usuario.setExpiraToken(null);

        usuarioRepository.save(usuario);
        return true;
    }

    private String gerarToken() {
        return UUID.randomUUID().toString();
    }
}
