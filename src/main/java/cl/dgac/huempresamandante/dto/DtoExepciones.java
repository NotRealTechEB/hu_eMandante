package cl.dgac.huempresamandante.dto;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DtoExepciones(
    LocalDateTime fecha,
    Integer codigoHttp,
    String error,
    String mensaje,
    Map<String, String> detalles,
    String ruta
) {

}
