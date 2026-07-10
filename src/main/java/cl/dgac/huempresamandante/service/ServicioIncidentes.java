package cl.dgac.huempresamandante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.dgac.huempresamandante.dto.DtoIncidentes;
import cl.dgac.huempresamandante.dto.DtoTipoIncidente;

@Service
public class ServicioIncidentes {

    private final WebClient cliente;

    public ServicioIncidentes (@Qualifier("incidentes")WebClient incidentes){
        this.cliente=incidentes;
    }

    public DtoIncidentes crearIncidentes(DtoIncidentes ex){
        return cliente.post().uri(
            uribuil -> uribuil .path("/api/v1.0/Incidentes/crearIncidente")
            .build()
        ).bodyValue(ex).retrieve().bodyToMono(DtoIncidentes.class).block();
    }
    public List<DtoTipoIncidente> tiposIncdentes (){
        List<DtoTipoIncidente> lista = cliente.get().uri(builder -> builder
            .path("/api/v1.0/Incidentes/listarTiposIncidentes")
            .build()
        ).retrieve().bodyToMono(new ParameterizedTypeReference <List<DtoTipoIncidente>>(){}).block();
        if (lista == null) {
        System.out.println("DEBUG: El microservicio devolvió null");
    } else {
        System.out.println("DEBUG: Se recibieron " + lista.size() + " elementos.");
        for (DtoTipoIncidente item : lista) {
            System.out.println("DEBUG: Item recibido -> " + item);
        }
    }
        return lista;
    }


    public List<String> despertar(){
        return cliente.get().uri(
        "/api/v1.0/Incidentes/despertar")
        .retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block();}

}
