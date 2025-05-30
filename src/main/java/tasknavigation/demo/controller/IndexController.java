// Raiz da API, aqui


package tasknavigation.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
   
    @GetMapping


    // Retorna mensagem e informações simples 
    //para saber se a API Esta de acordo e funcionando
    public String hello() {
        return "Estou na raiz da API TaskNavigation";
 
    }
 
 
 
 
}