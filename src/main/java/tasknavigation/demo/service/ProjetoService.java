package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Projeto;
import tasknavigation.demo.repository.ProjetoRepository;


import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    public Optional<Projeto> buscarPorId(Long id) {
        return projetoRepository.findById(id);
    }

    public Projeto salvar(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public void deletar(Long id) {
        projetoRepository.deleteById(id);
    }
}
