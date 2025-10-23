package tasknavigation.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.ConfiguracaoRepository;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.jwt.JwtService;


@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;

    public UsuarioService(
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            ConfiguracaoRepository configuracaoRepository, // ✅ injetar
            JdbcTemplate jdbcTemplate,
            EmailService emailService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.configuracaoRepository = configuracaoRepository; // ✅ salvar
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
    }

    public Usuario salvar(Usuario usuario) {
        // Salva o usuário
        Usuario novoUsuario = usuarioRepository.save(usuario);

        // 🔥 Cria configuração padrão
        Configuracao config = new Configuracao();
        config.setUsuario(novoUsuario);
        config.setFotoPerfil(null);       // sem foto
        config.setTema("claro");          // padrão
        config.setNotificacoes(true);     // padrão
        configuracaoRepository.save(config);

        return novoUsuario;
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



    public List<Usuario> buscarPorEquipe(Long equipeId) {
        return usuarioRepository.findByEquipeIdAndCodStatusTrue(equipeId);
    }

    // 🔥 Pega o usuário logado pelo SecurityContext
    public Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário logado não encontrado"));
    }
}
