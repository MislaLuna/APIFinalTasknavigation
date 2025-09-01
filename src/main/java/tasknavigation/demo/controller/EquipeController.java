package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.service.EquipeService;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/equipes")
@CrossOrigin(origins = "*") // ajuste para segurança depois
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    private String gerarCodigoConvite() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[12]; // tamanho do código
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Criar nova equipe */
    @PostMapping
    public ResponseEntity<?> criarEquipe(@RequestBody Equipe equipe) {
        if (equipe.getNome() == null || equipe.getNome().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome da equipe é obrigatório");
        }
        equipe.setCodigoConvite(gerarCodigoConvite());
        Equipe novaEquipe = equipeService.salvar(equipe);
        return ResponseEntity.ok(novaEquipe);
    }

    /** Gerar código de convite para equipe existente */
    @PostMapping("/gerar-convite")
    public ResponseEntity<?> gerarConvite(@RequestParam Long idEquipe) {
        Optional<Equipe> equipeOpt = equipeService.buscarPorId(idEquipe);
        if (equipeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Equipe não encontrada");
        }
        Equipe equipe = equipeOpt.get();
        equipe.setCodigoConvite(gerarCodigoConvite());
        equipeService.salvar(equipe);
        return ResponseEntity.ok(equipe.getCodigoConvite());
    }

    /** Listar usuários de uma equipe (apenas admin) */
    @GetMapping("/{id}/usuarios")
    public ResponseEntity<?> listarUsuarios(@PathVariable Long id) {
        Optional<Equipe> equipeOpt = equipeService.buscarPorId(id);
        if (equipeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Equipe não encontrada");
        }
        Equipe equipe = equipeOpt.get();
        List<Usuario> usuarios = equipe.getUsuarios(); // Certifique-se que Equipe tem List<Usuario> mapeado
        return ResponseEntity.ok(usuarios);
    }
}
