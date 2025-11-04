package tasknavigation.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.token.TokenRepository;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String path = request.getRequestURI();
        final String method = request.getMethod();

        // ✅ Ignora requisições pré-flight (CORS)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Libera rotas públicas
        if (isPublic(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        String userEmail = null;

        try {
            userEmail = jwtService.extractUsername(token);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao extrair usuário do token: " + e.getMessage());
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuario userDetails = (Usuario) this.userDetailsService.loadUserByUsername(userEmail);

            // ✅ Se o token não estiver no banco, considera válido (para ambiente local)
            boolean isTokenValidInDb = tokenRepository.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(true);

            try {
                if (jwtService.isTokenValid(token, userDetails) && isTokenValidInDb) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("🚫 Token inválido ou expirado para o usuário: " + userEmail);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Erro ao validar token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path, String method) {
        // Swagger / OpenAPI
        if (path.startsWith("/swagger") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars")
                || "/swagger-ui.html".equals(path)) return true;

        // Auth (se você tiver /auth/**)
        if (path.startsWith("/auth/")) return true;

        // Login e cadastro de usuários
        if ("/usuarios/login".equals(path) && "POST".equalsIgnoreCase(method)) return true;
        if ("/usuarios".equals(path) && "POST".equalsIgnoreCase(method)) return true;

        // Recuperação de senha e confirmação de email
        if (path.startsWith("/usuarios/enviar-codigo-recuperacao")) return true;
        if (path.startsWith("/usuarios/verificar-codigo")) return true;
        if (path.startsWith("/usuarios/redefinir-senha")) return true;
        if (path.startsWith("/usuarios/confirmar-email")) return true;

        // Raiz / actuator
        if ("/".equals(path) || path.startsWith("/actuator")) return true;

        return false;
    }
}
