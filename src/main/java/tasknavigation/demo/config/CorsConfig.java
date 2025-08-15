package tasknavigation.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // aplica para todas as rotas
                        .allowedOrigins("*") // permite todas as origens
                        .allowedMethods("*") // permite todos os métodos HTTP (GET, POST, etc)
                        .allowedHeaders("*"); // permite todos os headers
            }
        };
    }
}
