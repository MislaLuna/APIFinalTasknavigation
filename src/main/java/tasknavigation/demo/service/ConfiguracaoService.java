package tasknavigation.demo.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tasknavigation.demo.domain.Configuracao;
import tasknavigation.demo.repository.ConfiguracaoRepository;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    // Lista todas as configurações
    public List<Configuracao> listarConfiguracao() {
        return (List<Configuracao>) configuracaoRepository.findAll();
    }

    // Buscar uma configuração pelo ID
    public Optional<Configuracao> obterConfiguracaoId(Long id) {
        return configuracaoRepository.findById(id);
    }

    // Incluir nova configuração
    public Configuracao incluirConfiguracao(Configuracao configuracao) {
        return configuracaoRepository.save(configuracao);
    }
} 
    // Atualizar configuração existente
   