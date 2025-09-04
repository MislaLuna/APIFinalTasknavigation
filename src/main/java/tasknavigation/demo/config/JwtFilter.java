package tasknavigation.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tasknavigation.demo.util.JwtUtil;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Ignora favicon e arquivos estáticos
        if (path.equals("/favicon.ico") || path.startsWith("/static/")) {
            filterChain.doFilter(request, response);
            return;
        }

        long start = System.currentTimeMillis();
        System.out.println("JwtFilter: entrando na requisição " + path);

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = JwtUtil.validateToken(token);

            if (email == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou expirado");
                System.out.println("JwtFilter: token inválido em " + path);
                return;
            }

            // Autentica o usuário no contexto do Spring
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
        long duration = System.currentTimeMillis() - start;
        System.out.println("JwtFilter: requisição " + path + " processada em " + duration + "ms");
    }
}
