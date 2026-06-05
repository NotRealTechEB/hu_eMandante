package cl.dgac.huempresamandante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.dgac.huempresamandante.dto.DtoEmpresaProvedora;
import cl.dgac.huempresamandante.dto.DtoEmpresamandante;
import cl.dgac.huempresamandante.exepciones.ExepcionValidadora;

@Service
public class ServicioEmpresas {
    private final WebClient empresaMandante;
    private final WebClient empresaProvedora;
    public ServicioEmpresas (@Qualifier("empresaMandante")WebClient conexion,
    @Qualifier("empresaProvedora") WebClient conexion2){
        this.empresaMandante=conexion;
        this.empresaProvedora=conexion2;

    }

    public ResponseEntity<DtoEmpresamandante> validar(String rut){
        return empresaMandante.get()
        .uri(builder->builder
            .path("/api/v1.5/Emandante/buscaRut")
            .queryParam("rut", rut)
            .build()
        ).retrieve()
        .toEntity(DtoEmpresamandante.class)
        .block();
    }

    public DtoEmpresaProvedora validarProvedora(String rut){
        List<DtoEmpresaProvedora> lista = empresaProvedora.get().uri(
            builder-> builder
            .path("/api/empresas-proveedoras")
            .build()).retrieve()
            .bodyToMono(new ParameterizedTypeReference <List<DtoEmpresaProvedora>>() {}).block();
        for (DtoEmpresaProvedora i : lista) {
            if (i.rut().equals(rut)){
                return i;
            }
        }
        throw  new ExepcionValidadora("el rut "+rut+" no esta asociado a ninguna empresa");
    }

}
