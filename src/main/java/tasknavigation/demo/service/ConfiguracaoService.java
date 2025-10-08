package tasknavigation.demo.service;

import org.springframework.stereotype.Service;
import java.util.List;
import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.repository.ConfiguracaoRepository;

@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
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
}
