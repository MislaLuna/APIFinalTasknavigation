package tasknavigation.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.ConfiguracaoRepository;
import tasknavigation.demo.repository.UsuarioRepository;

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
            ConfiguracaoRepository configuracaoRepository,
            JdbcTemplate jdbcTemplate,
            EmailService emailService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
    }

    // ============================================================
    // 🔹 SALVAR ou ATUALIZAR USUÁRIO + CONFIGURAÇÃO
    // ============================================================
  @Transactional
public Usuario salvar(Usuario usuario) {
    Usuario novoUsuario = usuarioRepository.save(usuario);

    // Busca configuração existente
    Optional<Configuracao> configExistenteOpt = configuracaoRepository.findByUsuario_Id(novoUsuario.getId());

    if (usuario.getConfiguracao() != null) {
        Configuracao novaConfig = usuario.getConfiguracao();

        if (configExistenteOpt.isPresent()) {
            Configuracao configExistente = configExistenteOpt.get();
            configExistente.setTema(novaConfig.getTema());
            configExistente.setFotoPerfil(novaConfig.getFotoPerfil());
            configExistente.setPosicaoFoto(novaConfig.getPosicaoFoto());
            configExistente.setNotificacoes(novaConfig.getNotificacoes());
            configuracaoRepository.save(configExistente);
        } else {
            // Cria nova configuração apenas se não existir
            novaConfig.setUsuario(novoUsuario);
            configuracaoRepository.save(novaConfig);
        }
    } else {
        // Se não veio configuração, cria padrão somente se não existir
        if (configExistenteOpt.isEmpty()) {
            Configuracao config = new Configuracao();
            config.setUsuario(novoUsuario);
            config.setTema("claro");
            config.setFotoPerfil(null);
            config.setPosicaoFoto(1);
            config.setNotificacoes(true);
            configuracaoRepository.save(config);
        }
    }

    return novoUsuario;
}



@Transactional
public Usuario salvarSemAlterarConfiguracao(Usuario usuario) {
    // Garante que não há referência à configuração (evita cascade persist)
    usuario.setConfiguracao(null);
    return usuarioRepository.save(usuario);
}


    // ============================================================
    // 🔹 LISTAGENS E BUSCAS
    // ============================================================
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

    // ============================================================
    // 🔹 PEGAR USUÁRIO LOGADO
    // ============================================================
    public Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário logado não encontrado"));
    }
}
