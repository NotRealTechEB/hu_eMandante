package cl.dgac.huempresamandante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class Conexiones {
    @Bean(name= "empresaMandante")
    public WebClient empresaMnadante(){
        return WebClient.builder()
        .baseUrl("https://empresamandante-1.onrender.com")
        .build();}
    
    @Bean(name="solicitudes")
    public WebClient solicitudes(){
        return WebClient.builder()
        .baseUrl("https://solicitudes-e4e1.onrender.com")
        .build();
    }

    @Bean(name= "empresaProvedora")
    public WebClient empresaProvedora(){
        return WebClient.builder()
        .baseUrl("https://empresaproveedora1.onrender.com")
        .build();
    }

    @Bean(name= "incidentes")
    public WebClient incidentes(){
        return WebClient.builder()
        .baseUrl("https://incidentes.onrender.com")
        .build();
    }

    @Bean(name= "registroVuelo")
    public WebClient registroVuelo(){
        return WebClient.builder()
        .baseUrl("https://registrovuelo.onrender.com/")
        .build();
    }
    @Bean(name= "tipoTrabajo")
    public WebClient tipoTrabajo (){
        return WebClient.builder()
        .baseUrl("https://tipotrabajo.onrender.com/")
        .build();
    }

}
