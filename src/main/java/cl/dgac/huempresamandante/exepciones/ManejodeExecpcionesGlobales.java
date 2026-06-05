package cl.dgac.huempresamandante.exepciones;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.dgac.huempresamandante.dto.DtoExepciones;

@ControllerAdvice
public class ManejodeExecpcionesGlobales {
    private ResponseEntity<DtoExepciones> construirDto(HttpStatus status,
        String mensaje,WebRequest request){
            String ruta =request.getDescription(false).replace("uri=", "");
            DtoExepciones dto =new DtoExepciones(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                ruta
            ); 
            return ResponseEntity.status(status).body(dto);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<DtoExepciones> microfueraServicio(WebClientRequestException ex, WebRequest request){
        return construirDto(HttpStatus.BAD_GATEWAY,"microservicio apagado", request);
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> erroresMicro(WebClientResponseException ex){
        try{
        DtoExepciones jsonMicro= ex.getResponseBodyAs(DtoExepciones.class);
        return ResponseEntity.status(ex.getStatusCode())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(jsonMicro);
        }catch(Exception e){
            return ResponseEntity.status(ex.getStatusCode())
                .body("Error en microservicio: " + ex.getResponseBodyAsString());
        }
    }
    @ExceptionHandler(ExepcionValidadora.class)
    public ResponseEntity<String> errorValidador(ExepcionValidadora ex){
        return new ResponseEntity<String> (ex.toString(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", "400");
        return ResponseEntity.badRequest().body(error);
    }
}



