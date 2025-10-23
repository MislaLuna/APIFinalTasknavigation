package tasknavigation.demo.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
 
import java.util.List;
 
@Configuration
public class CorsConfig {
 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
 
        // 🔹 Permitir front web e mobile (qualquer porta local)
        config.setAllowedOriginPatterns(List.of(    
            "http://localhost:*",       // Qualquer porta do localhost
            "http://172.19.1.66:*"      // Flutter mobile, qualquer porta
        ));
 
        // 🔹 Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
 
        // 🔹 Headers permitidos
        config.setAllowedHeaders(List.of("*")); // Permite qualquer header
 
        // 🔹 Permitir envio de cookies/credenciais
        config.setAllowCredentials(true);
 
        // 🔹 Registrar para todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
 
        return source;
    }
}
 
 