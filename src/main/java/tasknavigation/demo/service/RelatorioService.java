package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Relatorio;
import tasknavigation.demo.repository.RelatorioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public List<Relatorio> listarTodos() {
        return relatorioRepository.findAll();
    }

    public Optional<Relatorio> buscarPorId(Long id) {
        return relatorioRepository.findById(id);
    }

    public Relatorio salvar(Relatorio relatorio) {
        return relatorioRepository.save(relatorio);
    }

    public void deletar(Long id) {
        relatorioRepository.deleteById(id);
    }
}
