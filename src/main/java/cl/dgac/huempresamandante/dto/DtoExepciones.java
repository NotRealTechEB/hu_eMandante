package cl.dgac.huempresamandante.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DtoExepciones(
    @JsonProperty("fecha")
    LocalDateTime tiempo,
    @JsonProperty("codigoHttp")
    Integer codigoEstado,
    String error,
    String mensaje,
    String ruta
) {

}
