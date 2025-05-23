package tasknavigation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Documento;
import tasknavigation.demo.repository.DocumentoRepository;

import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Iterable<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    public Optional<Documento> buscarPorId(Integer id) {
        return documentoRepository.findById(id);
    }

    public Documento salvar(Documento documento) {
        return documentoRepository.save(documento);
    }

    public void deletarPorId(Integer id) {
        documentoRepository.deleteById(id);
    }
}
