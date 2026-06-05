package cl.dgac.huempresamandante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.dgac.huempresamandante.dto.DtoSolicitudes;

@Service
public class ServicioSolicitud {
    private final WebClient solicitudes;
    
    public ServicioSolicitud (@Qualifier("solicitudes") WebClient conexion){
        this.solicitudes=conexion;
    }
    public DtoSolicitudes crearSoicitud(DtoSolicitudes ex ){
        return solicitudes.post().uri("/api/v1.5/solicitudes/crearSolicitud")
        .bodyValue(ex).retrieve().bodyToMono(DtoSolicitudes.class).block();
    }
    public List<DtoSolicitudes> listarSolicitudesempresa(String rut){
        return solicitudes.get().uri(uriBuilder-> uriBuilder 
            .path("/api/v1.5/solicitudes/Empresamandnate")
            .queryParam("rut", rut)
            .build()
        ).retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<DtoSolicitudes>> () {} ).block();
    }

}
