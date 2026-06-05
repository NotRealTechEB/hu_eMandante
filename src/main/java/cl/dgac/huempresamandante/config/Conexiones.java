package cl.dgac.huempresamandante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class Conexiones {
    @Bean(name= "empresaMandante")
    public WebClient empresaMnadante(){
        return WebClient.builder()
        .baseUrl("http://localhost:8088")
        .build();}
    
    @Bean(name="solicitudes")
    public WebClient solicitudes(){
        return WebClient.builder()
        .baseUrl("http://localhost:8080")
        .build();
    }

    @Bean(name= "empresaProvedora")
    public WebClient empresaProvedora(){
        return WebClient.builder()
        .baseUrl("http://localhost:8081")
        .build();
    }

    @Bean(name= "incidentes")
    public WebClient incidentes(){
        return WebClient.builder()
        .baseUrl("http://localhost:8091")
        .build();
    }

    @Bean(name= "registroVuelo")
    public WebClient registroVuelo(){
        return WebClient.builder()
        .baseUrl("http://localhost:8084")
        .build();
    }
}
