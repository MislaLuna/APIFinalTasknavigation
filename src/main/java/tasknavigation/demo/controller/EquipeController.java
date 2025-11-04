package tasknavigation.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tasknavigation.demo.domain.Equipe;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.domain.enums.NivelAcesso;
import tasknavigation.demo.repository.EquipeRepository;
import tasknavigation.demo.service.EmailService;
import tasknavigation.demo.service.EquipeService;
import tasknavigation.demo.service.UsuarioService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EquipeRepository equipeRepository;

    // 🔹 Gera código aleatório para convite
    private String gerarCodigoConvite() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[12];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // 🔹 Listar todas as equipes
    @GetMapping
    public ResponseEntity<List<Equipe>> listarEquipes() {
        return ResponseEntity.ok(equipeService.buscarTodas());
    }

    // 🔹 Criar nova equipe (seta o criador com o usuário logado)
@PostMapping
public ResponseEntity<?> criarEquipe(@RequestBody Equipe equipe, @AuthenticationPrincipal Usuario usuarioLogado) {
    if (equipe.getNome() == null || equipe.getNome().isEmpty()) {
        return ResponseEntity.badRequest().body("Nome da equipe é obrigatório");
    }

    // 🔹 Busca o usuário completo no banco
    Usuario criador = usuarioService.buscarPorEmail(usuarioLogado.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    equipe.setCodigoConvite(gerarCodigoConvite());
    equipe.setCriador(criador);

    Equipe novaEquipe = equipeService.salvar(equipe);

    return ResponseEntity.ok(novaEquipe);
}


    // 🔹 Entrar na equipe usando código de convite
    @PostMapping("/entrar")
    public ResponseEntity<?> entrarEquipe(@RequestBody Map<String, String> body) {
        String codigo = body.get("codigoConvite");
        if (codigo == null || codigo.isEmpty()) {
            return ResponseEntity.badRequest().body("Código de convite é obrigatório");
        }

        Optional<Equipe> equipeOpt = equipeService.buscarPorCodigoConvite(codigo);
        if (equipeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Equipe não encontrada");
        }

        Equipe equipe = equipeOpt.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        boolean jaMembro = equipe.getUsuarios().stream()
                .anyMatch(u -> u.getId().equals(usuario.getId()));

        if (!jaMembro) {
            usuario.setEquipe(equipe);
            usuarioService.salvar(usuario);
            equipe.getUsuarios().add(usuario);
        }

        Map<String, Object> equipeMap = Map.of(
                "nome", equipe.getNome(),
                "descricao", equipe.getDescricao() != null ? equipe.getDescricao() : "",
                "membros", equipe.getUsuarios().stream()
                        .map(u -> Map.of("nome", u.getNome(), "email", u.getEmail()))
                        .toList()
        );

        return ResponseEntity.ok(Map.of("equipe", equipeMap));
    }

    // 🔹 Listar equipes do usuário logado (onde ele é o criador)
@GetMapping("/minhas")
public ResponseEntity<List<Equipe>> listarEquipesDoUsuario(@AuthenticationPrincipal Usuario usuario) {
    List<Equipe> equipesCriadas = equipeRepository.findByCriadorId(usuario.getId());
    List<Equipe> equipesParticipando = equipeRepository.findByUsuariosId(usuario.getId());

    Set<Equipe> todasEquipes = new HashSet<>();
    todasEquipes.addAll(equipesCriadas);
    todasEquipes.addAll(equipesParticipando);

    return ResponseEntity.ok(new ArrayList<>(todasEquipes));
}


    // 🔹 Convidar colaborador para equipe
    @PostMapping("/{id}/convidar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    public ResponseEntity<?> convidarColaborador(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String email = body.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }

        Optional<Equipe> equipeOpt = equipeService.buscarPorId(id);
        if (equipeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Equipe não encontrada");
        }
        Equipe equipe = equipeOpt.get();

        // Busca usuário, e se não existir, cria um novo
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseGet(() -> {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    novoUsuario.setNome(email.split("@")[0]); // nome padrão
                    novoUsuario.setNivelAcesso(NivelAcesso.USUARIO);
                    return usuarioService.salvar(novoUsuario);
                });

        // Verifica se já é membro
        boolean jaMembro = equipe.getUsuarios().stream()
                .anyMatch(u -> u.getId().equals(usuario.getId()));
        if (jaMembro) {
            return ResponseEntity.badRequest().body("Usuário já é membro da equipe");
        }

        // Adiciona usuário à equipe
        usuario.setEquipe(equipe);
        usuarioService.salvar(usuario);
        equipe.getUsuarios().add(usuario);

        // 🔹 Envia o convite por e-mail
        emailService.enviarConviteColaborador(email, equipe.getNome(), equipe.getCodigoConvite());

        Map<String, Object> equipeMap = Map.of(
                "nome", equipe.getNome(),
                "descricao", equipe.getDescricao() != null ? equipe.getDescricao() : "",
                "membros", equipe.getUsuarios().stream()
                        .map(u -> Map.of("nome", u.getNome(), "email", u.getEmail()))
                        .toList()
        );

        return ResponseEntity.ok(Map.of("equipe", equipeMap, "mensagem", "Convite enviado com sucesso"));
    }

    // 🔹 Deletar equipe (só ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletarEquipe(@PathVariable Long id) {
        Optional<Equipe> equipeOpt = equipeService.buscarPorId(id);
        if (equipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        equipeService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Equipe deletada com sucesso"));
    }
}
