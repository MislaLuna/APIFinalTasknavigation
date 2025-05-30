// RODAR TODA A APLICAÇÃO
<<<<<<< HEAD
// processa dados, realiza cálculos, interage com o banco 
// de dados e executa as operações
// principais que suportam a funcionalidade da aplicação.
=======
//processa dados, realiza cálculos, interage com o banco 
//de dados e executa as operações
//principais que suportam a funcionalidade da aplicação.

>>>>>>> 36a922dfcf7c5aa1011d21c59e9ec32ed9d9f1c0

package tasknavigation.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
=======
>>>>>>> 36a922dfcf7c5aa1011d21c59e9ec32ed9d9f1c0

@SpringBootApplication
public class TasknavigationApplication {

<<<<<<< HEAD
    public static void main(String[] args) {
        SpringApplication.run(TasknavigationApplication.class, args);
    }

}

// Classe de configuração para CORS
@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas as rotas
                .allowedOrigins("http://localhost:5173") // URL do seu frontend Vite
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowCredentials(true); // Permitir cookies
    }
}
	
=======
	public static void main(String[] args) {
		SpringApplication.run(TasknavigationApplication.class, args);
	}

}
>>>>>>> 36a922dfcf7c5aa1011d21c59e9ec32ed9d9f1c0
