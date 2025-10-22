package tasknavigation.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import tasknavigation.demo.exceptions.CustomAccessDeniedHandler;
import tasknavigation.demo.exceptions.CustomAuthenticationEntryPoint;
import tasknavigation.demo.jwt.JwtAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static tasknavigation.demo.domain.enums.Permission.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/index",
            "/images/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
                                 AuthenticationProvider authenticationProvider,
                                 LogoutHandler logoutHandler,
                                 CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                 CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .authorizeHttpRequests(req -> req
                        // URLs públicas
                        .requestMatchers("/auth/**", "/usuarios/**").permitAll()
                        .requestMatchers(WHITE_LIST_URL).permitAll()

                        // ✅ Permissões detalhadas para tarefas
                        .requestMatchers(POST, "/tarefas/**").hasAnyAuthority(USUARIO_CREATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(GET, "/tarefas/**").hasAnyAuthority(USUARIO_READ.name(), ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/tarefas/**").hasAnyAuthority(USUARIO_UPDATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(DELETE, "/tarefas/**").hasAnyAuthority(USUARIO_DELETE.name(), ADMIN_CREATE.name())

                        // ✅ Permissões detalhadas para projetos
                        .requestMatchers(POST, "/projetos/**").hasAnyAuthority(USUARIO_CREATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(GET, "/projetos/**").hasAnyAuthority(USUARIO_READ.name(), ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/projetos/**").hasAnyAuthority(USUARIO_UPDATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(DELETE, "/projetos/**").hasAnyAuthority(USUARIO_DELETE.name(), ADMIN_CREATE.name())

                        // qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((req, res, auth) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
