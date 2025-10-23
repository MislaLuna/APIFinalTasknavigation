package tasknavigation.demo.service;

import org.springframework.stereotype.Service;
import java.util.List;

import jakarta.annotation.PostConstruct; // opcional
import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.repository.ConfiguracaoRepository;
import tasknavigation.demo.repository.UsuarioRepository;

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

    public Configuracao salvar(Configuracao configuracao) {
        return configuracaoRepository.save(configuracao);
    }

    public Configuracao buscarPorId(Long id) {
        return configuracaoRepository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        configuracaoRepository.deleteById(id);
    }

    // 🔥 Método para criar configuração padrão para um usuário
    public Configuracao criarConfiguracaoPadraoParaUsuario(Usuario usuario) {
        // Verifica se já existe
        Configuracao existente = configuracaoRepository.findByUsuarioId(usuario.getId());
        if (existente != null) return existente;

        // Cria configuração padrão
        Configuracao config = new Configuracao();
        config.setUsuario(usuario);
        config.setFotoPerfil(null);      // sem foto
        config.setTema("claro");         // padrão
        config.setNotificacoes(true);    // padrão
        return configuracaoRepository.save(config);
    }

    // Opcional: criar configurações para todos os usuários que ainda não têm
    @PostConstruct
    public void criarConfiguracoesPadrao() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            criarConfiguracaoPadraoParaUsuario(u);
        }
        System.out.println("Configurações padrão criadas para usuários existentes.");
    }
}
