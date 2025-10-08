package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.repository.EquipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    // Salva ou atualiza uma equipe
    public Equipe salvar(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    // Busca equipe pelo ID
    public Optional<Equipe> buscarPorId(Long id) {
        return equipeRepository.findById(id);
    }

    // Busca equipe pelo código de convite
    public Optional<Equipe> buscarPorCodigoConvite(String codigoConvite) {
        return equipeRepository.findByCodigoConvite(codigoConvite);
    }

    // Lista todas as equipes
    public List<Equipe> listarEquipes() {
        return equipeRepository.findAll();
    }
    
    // Busca todas as equipes
    public List<Equipe> buscarTodas() {
        return equipeRepository.findAll();
    }

    // Deleta equipe pelo ID
    public boolean deletarPorId(Long id) {
        if (equipeRepository.existsById(id)) {
            equipeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Deleta equipe
    public void deletar(Long id) {
        equipeRepository.deleteById(id);
    }
}
