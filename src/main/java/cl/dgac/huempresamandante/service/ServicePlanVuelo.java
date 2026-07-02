package cl.dgac.huempresamandante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.dgac.huempresamandante.dto.DtoPlandeVuelo;

@Service
public class ServicePlanVuelo {
    private final WebClient planVuelo;
    public ServicePlanVuelo (@Qualifier("registroVuelo") WebClient planVuelo){
        this.planVuelo=planVuelo;
    }
    
    public List<DtoPlandeVuelo> planesVuelo (String rut){
        return planVuelo.get().uri(
            builder -> builder .path("/api/v1/planvuelo/listarPoRut").queryParam("rut", rut)
            .build()
        ).retrieve().bodyToMono(new ParameterizedTypeReference<List<DtoPlandeVuelo>>(){}).block();
    }
}
