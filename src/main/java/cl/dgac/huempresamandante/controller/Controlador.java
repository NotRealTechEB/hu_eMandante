package cl.dgac.huempresamandante.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;
import cl.dgac.huempresamandante.dto.DtoEmpresamandante;
import cl.dgac.huempresamandante.dto.DtoIncidentes;
import cl.dgac.huempresamandante.dto.DtoPlandeVuelo;
import cl.dgac.huempresamandante.dto.DtoSolicitudes;
import cl.dgac.huempresamandante.dto.DtoTipoIncidente;

import cl.dgac.huempresamandante.exepciones.ExepcionValidadora;
import cl.dgac.huempresamandante.service.ServicePlanVuelo;
import cl.dgac.huempresamandante.service.ServicioEmpresas;
import cl.dgac.huempresamandante.service.ServicioIncidentes;
import cl.dgac.huempresamandante.service.ServicioSolicitud;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/huempresamandate")
public class Controlador {
    private final ServicioEmpresas  servicio;
    private final ServicioSolicitud servicioSolicitud;
    private final ServicioIncidentes servicioIncidentes;
    private final ServicePlanVuelo servicePlanVuelo;
    private  DtoEmpresamandante dtoEmpresa;

    public Controlador (ServicioEmpresas servicio, ServicioSolicitud servicioSolicitud,ServicioIncidentes servicioIncidentes,ServicePlanVuelo planVuelo){
        this.servicio= servicio;
        this.servicioSolicitud= servicioSolicitud;
        this.servicioIncidentes=servicioIncidentes;
        this.servicePlanVuelo=planVuelo;
        this.dtoEmpresa= null;
    }

    @GetMapping("/despertar")
    public Mono<ResponseEntity<List<String>>> despertar() { // <-- El Mono va afuera de todo
        return Mono.zip(
            servicioIncidentes.despertar(), // Devuelve Mono<List<String>>
            servicioSolicitud.despertar(),  // Devuelve Mono<String>
            servicio.despertar()            // Devuelve Mono<String>
        ).map(tuple -> {
            List<String> miki = new ArrayList<>();
            
            // 1. Extraemos la lista del primer servicio de forma segura
            List<String> incidentes = (List<String>) tuple.getT1();
            if (incidentes != null) {
                miki.addAll(incidentes);
            }
            
            // 2. Extraemos los Strings de los otros servicios usando las posiciones de la tupla
            String resultadoSolicitud = (String) tuple.getT2();
            String resultadoEmpresa = (String) tuple.getT3();
            
            miki.add("servicio solicitud : " + resultadoSolicitud);
            miki.add("servicio empresa Mandante : " + resultadoEmpresa);
            
            // 3. Devolvemos la lista final envuelta en el ResponseEntity correcto
            return new ResponseEntity<>(miki, HttpStatus.OK);
        });
}

    
    @GetMapping("/validar")
    public ResponseEntity<DtoEmpresamandante> validar(@RequestParam(name = "rut") String rut) {
        this.dtoEmpresa = servicio.validar(rut).getBody();
        return new ResponseEntity<DtoEmpresamandante>(dtoEmpresa,HttpStatus.OK);
    }
    //parte de solicitud
    @GetMapping("/Solicitudes")
    public ResponseEntity<List<DtoSolicitudes>> misSolicitudes() {
        if (dtoEmpresa == null){
            throw new ExepcionValidadora("deves primero validar la empresa en /api/v1/huempresamandate/validar?rut=xxxxx de esa manera");
        }
        return new ResponseEntity<List<DtoSolicitudes>>(servicioSolicitud.listarSolicitudesempresa(dtoEmpresa.rut()),HttpStatus.OK);
    }

    @PostMapping("/crearSolicitud")
    public ResponseEntity<DtoSolicitudes> crearSolicitud(@RequestBody DtoSolicitudes entity) {
        if (dtoEmpresa == null){
            throw new ExepcionValidadora("deves primero validar la empresa en /api/v1/huempresamandate/validar?rut=xxxxx de esa manera");
        }
        
        DtoSolicitudes dto = new DtoSolicitudes(
            null,
            entity.descripcion(),
            entity.peso(),
            entity.ubicacion(),
            entity.atendiada(),
            entity.tipo(),
            dtoEmpresa.rut(),
            servicio.validarProvedora(entity.rutEmpresaProveedora()).rut(),
            entity.region()
        );
        return new ResponseEntity<DtoSolicitudes>(servicioSolicitud.crearSoicitud(dto),HttpStatus.CREATED); 
    }
/// incidentes
    @PostMapping("/crearincidentes")
    public ResponseEntity<DtoIncidentes> Incidentes(
    @RequestBody DtoIncidentes entity) { 
        System.out.println("el dto que ingresa es " + entity.toString());
        String tipo = entity.tipo().tipo();
        DtoTipoIncidente dtoTipo = new DtoTipoIncidente(tipo,null);
        if (dtoEmpresa==null){
            DtoIncidentes dto = new DtoIncidentes(
                null,
                entity.descripcion(),
                dtoTipo,
                "ANONIMO",
                LocalDateTime.now(),
                entity.resuelto(),
                entity.region()
            );
            System.out.println(dto);
            return new ResponseEntity<DtoIncidentes>(servicioIncidentes.crearIncidentes(dto), HttpStatus.OK) ;
        }
        DtoIncidentes dto = new DtoIncidentes(
                null,
                entity.descripcion(),
                dtoTipo,
                dtoEmpresa.nombre().toUpperCase(),
                entity.fecha_reporte(),
                entity.resuelto(),
                entity.region()
            );
        System.out.println(dto);
        return new ResponseEntity<DtoIncidentes>(servicioIncidentes.crearIncidentes(dto), HttpStatus.OK) ;
    }
    @GetMapping("/tiposIncidentes")
    public ResponseEntity<List<DtoTipoIncidente>> List() {
        return new ResponseEntity<List<DtoTipoIncidente>>(servicioIncidentes.tiposIncdentes()
        ,HttpStatus.OK);
    }
////________________________listaDe planes de Vuelo_____________//
    @GetMapping("/listarPlanesVuelo")
    public ResponseEntity<List<DtoPlandeVuelo>> lsitaPlanVuelo (@RequestParam(name = "rut",required = false) String rut) {
        if ((dtoEmpresa==null)&& (rut.isBlank())){
            throw new ExepcionValidadora("deves primero validar la empresa en /api/v1/huempresamandate/validar?rut=xxxxx de esa manera o agregar el rut de la siguiente manera ?rut=xxxx");
        }
        else if (rut.isBlank()){
            return new ResponseEntity<List<DtoPlandeVuelo>> (servicePlanVuelo.planesVuelo(dtoEmpresa.rut()),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<List<DtoPlandeVuelo>> (servicePlanVuelo.planesVuelo(rut),HttpStatus.OK);
        }
    }
    
    
    

}
