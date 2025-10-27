package tasknavigation.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.ConfiguracaoRepository;
import tasknavigation.demo.repository.UsuarioRepository;

import java.util.List;

@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;
    private final UsuarioRepository usuarioRepository;

    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository,
                               UsuarioRepository usuarioRepository) {
        this.configuracaoRepository = configuracaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Configuracao> listarConfiguracao() {
        return configuracaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Configuracao getOrCreateByUsuarioId(Long usuarioId) {
        return configuracaoRepository.findByUsuario_Id(usuarioId)
                .orElseGet(() -> criarDefaultESalvar(usuarioId));
    }

    @Transactional
    public Configuracao upsertByUsuarioId(Long usuarioId, Configuracao entrada) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        Configuracao cfg = configuracaoRepository.findByUsuario_Id(usuarioId)
                .orElseGet(() -> {
                    Configuracao c = new Configuracao();
                    c.setUsuario(usuario);
                    c.setTema("claro");
                    c.setNotificacoes(true);
                    c.setPosicaoFoto(1);
                    c.setFotoPerfil(null);
                    return c;
                });

        // Atualiza apenas o que veio no corpo (evita NPE)
        if (entrada.getTema() != null) cfg.setTema(entrada.getTema());
        if (entrada.getNotificacoes() != null) cfg.setNotificacoes(entrada.getNotificacoes());
        if (entrada.getFotoPerfil() != null) cfg.setFotoPerfil(entrada.getFotoPerfil());
        if (entrada.getPosicaoFoto() != null) cfg.setPosicaoFoto(entrada.getPosicaoFoto());

        cfg.setUsuario(usuario);
        return configuracaoRepository.save(cfg);
    }

    private Configuracao criarDefaultESalvar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        Configuracao config = new Configuracao();
        config.setUsuario(usuario);
        config.setFotoPerfil(null);
        config.setTema("claro");
        config.setNotificacoes(true);
        config.setPosicaoFoto(1);
        return configuracaoRepository.save(config);
    }

    public void deletar(Long id) {
        configuracaoRepository.deleteById(id);
    }
}
