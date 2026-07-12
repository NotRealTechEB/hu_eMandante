package cl.dgac.huempresamandante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.dgac.huempresamandante.dto.DtoSolicitudes;
import cl.dgac.huempresamandante.dto.DtoTipoTrabajo;
import reactor.core.publisher.Mono;

@Service
public class ServicioSolicitud {
    private final WebClient solicitudes;
    private final WebClient tipoTrabajo;
    
    public ServicioSolicitud (@Qualifier("solicitudes") WebClient conexion,
    @Qualifier("tipoTrabajo")WebClient conexionPlus){
        this.solicitudes=conexion;
        this.tipoTrabajo=conexionPlus;
    }
    public DtoSolicitudes crearSoicitud(DtoSolicitudes ex ){
        return solicitudes.post().uri("/api/v1.6x/solicitudes/crearSolicitud")
        .bodyValue(ex).retrieve().bodyToMono(DtoSolicitudes.class).block();
    }
    public List<DtoSolicitudes> listarSolicitudesempresa(String rut){
        return solicitudes.get().uri(uriBuilder-> uriBuilder 
            .path("/api/v1.6x/solicitudes/Empresamandnate")
            .queryParam("rut", rut)
            .build()
        ).retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<DtoSolicitudes>> () {} ).block();
    }
    public Mono<String> despertar(){
        return solicitudes.get().uri(
        "/api/v1.6x/solicitudes/despertar")
        .retrieve().bodyToMono(String.class)
        .onErrorReturn("");}

    public List<DtoTipoTrabajo> tiposDetrabajo()
    {
        return tipoTrabajo.get().uri(builder -> builder
            .path("/api/tipos-trabajo").build()
        ).retrieve().bodyToMono(new ParameterizedTypeReference <List<DtoTipoTrabajo>>() {}).block();
    }
}
