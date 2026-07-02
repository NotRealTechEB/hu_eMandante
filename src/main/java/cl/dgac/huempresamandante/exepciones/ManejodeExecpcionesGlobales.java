package cl.dgac.huempresamandante.exepciones;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.dgac.huempresamandante.dto.DtoExepciones;
import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.JsonNode;

@ControllerAdvice
public class ManejodeExecpcionesGlobales {
    private ResponseEntity<DtoExepciones> buildResponse(
        HttpStatus status,String mnesaje,
        String ruta,Map<String, String> detalles){
        DtoExepciones dto = new DtoExepciones(
            LocalDateTime.now(),status.value(),
            status.getReasonPhrase(),mnesaje,
            detalles,ruta);
            System.out.println(dto);
            return ResponseEntity.status(status).body(dto);
        }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<DtoExepciones> microfueraServicio(WebClientRequestException ex){
        return buildResponse(HttpStatus.BAD_GATEWAY,
            "error en micro externo", 
            ex.getUri().toString(), 
            null);}
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> erroresMicro(WebClientResponseException ex){
        System.out.println(ex.getResponseBodyAsString());
        JsonNode jsonNode = ex.getResponseBodyAs(JsonNode.class);
        System.out.println(jsonNode);
        return ResponseEntity.status(ex.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(jsonNode);
        }
    @ExceptionHandler(ExepcionValidadora.class)
    public ResponseEntity<DtoExepciones> errorValidador(ExepcionValidadora ex, HttpServletRequest request) {
        System.out.println("EXEPCION VALIDADORA");
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DtoExepciones> handleRuntime(RuntimeException ex,HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Error Interno del Servidor", request.getRequestURI(), null);
    }

}



