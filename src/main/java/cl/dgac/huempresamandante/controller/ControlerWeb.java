package cl.dgac.huempresamandante.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.ui.Model;
import cl.dgac.huempresamandante.dto.DtoEmpresamandante;
import cl.dgac.huempresamandante.dto.DtoIncidentes;
import cl.dgac.huempresamandante.dto.DtoSolicitudes;
import cl.dgac.huempresamandante.dto.DtoTipoIncidente;
import cl.dgac.huempresamandante.dto.DtoTipoTrabajo;
import cl.dgac.huempresamandante.exepciones.ExepcionValidadora;
import cl.dgac.huempresamandante.service.ServicePlanVuelo;
import cl.dgac.huempresamandante.service.ServicioEmpresas;
import cl.dgac.huempresamandante.service.ServicioIncidentes;
import cl.dgac.huempresamandante.service.ServicioSolicitud;

@SessionScope
@Controller
public class ControlerWeb {
private final ServicioEmpresas  servicio;
    private final ServicioSolicitud servicioSolicitud;
    private final ServicioIncidentes servicioIncidentes;
    private final ServicePlanVuelo servicePlanVuelo;
    private  DtoEmpresamandante dtoEmpresa;

    public ControlerWeb (ServicioEmpresas servicio, ServicioSolicitud servicioSolicitud,ServicioIncidentes servicioIncidentes,ServicePlanVuelo planVuelo){
        this.servicio= servicio;
        this.servicioSolicitud= servicioSolicitud;
        this.servicioIncidentes=servicioIncidentes;
        this.servicePlanVuelo=planVuelo;
        this.dtoEmpresa= null;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("empresaValidada", this.dtoEmpresa != null);
        model.addAttribute("dtoEmpresa", this.dtoEmpresa);  
        return "index"; 
    }

    @GetMapping("/validar")
    public String validar(@RequestParam("rut") String rut, Model model) {
        this.dtoEmpresa = servicio.validar(rut).getBody();
        return "redirect:/"; 
    }

    @GetMapping("/logout")
    public String logout() {
        this.dtoEmpresa = null; 
        return "redirect:/"; 
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        if (this.dtoEmpresa == null) {
            return "redirect:/"; 
        }
        List<DtoSolicitudes> lista = servicioSolicitud.listarSolicitudesempresa(dtoEmpresa.rut());
        model.addAttribute("listaSolicitudes", lista);
        model.addAttribute("empresa", dtoEmpresa);
        
        return "dashboard"; 
    }
    @GetMapping("/formulario-solicitud")
public String mostrarFormulario(Model model) {
    if (this.dtoEmpresa == null) return "redirect:/";

    List<DtoTipoTrabajo> tipos = servicioSolicitud.tiposDetrabajo(); 
    model.addAttribute("listaTipos", tipos);
    model.addAttribute("rutMandante", this.dtoEmpresa.rut());
    return "solicitudCrear";
}
@PostMapping("/crearSolicitud")
public String crearSolicitud(@ModelAttribute DtoSolicitudes entity) {
    // 1. Validación de sesión (ya la tenías)
    if (dtoEmpresa == null) {
        throw new ExepcionValidadora("Debes validar la empresa mandante primero.");
    }
    
    // 2. Validación de proveedora (Si falla, lanza excepción y el ControllerAdvice la captura)
    // Asumo que validarProvedora devuelve el objeto o lanza excepción si no existe
    var proveedora = servicio.validarProvedora(entity.rutEmpresaProveedora());
    
    // 3. Crear el DTO con los datos seguros
    DtoSolicitudes dto = new DtoSolicitudes(
        null, // ID (autogenerado en BD)
        entity.descripcion(),
        entity.peso(),
        entity.ubicacion(),
        false, // Forzamos a FALSE como pediste
        entity.tipo(),
        dtoEmpresa.rut(), // Sacado de la sesión, no del form
        proveedora.rut(), // Validado contra la BD
        entity.region()
    );
    
    servicioSolicitud.crearSoicitud(dto);
    
    return "redirect:/dashboard";}

    // 1. Mostrar el formulario
    @GetMapping("/formulario-incidente")
    public String mostrarFormularioIncidente(Model model) {
        // Obtenemos la lista de objetos DtoTipoIncidente
        List<DtoTipoIncidente> tipos = servicioIncidentes.tiposIncdentes();
        model.addAttribute("listaTipos", tipos);
        // Si hay empresa, pasamos el nombre, si no, será ANÓNIMO
        model.addAttribute("empresaNombre", (this.dtoEmpresa != null) ? this.dtoEmpresa.nombre() : "ANONIMO");
        return "incidenteCrear";
    }


    @PostMapping("/crearIncidentes")
public String crearIncidente(
    @RequestParam("descripcion") String descripcion,
    @RequestParam("region") String region,
    @RequestParam("tipoSeleccionado") String nombreTipo) { // Capturamos el String del select
    
    // 1. Reconstruimos el objeto tipo manualmente
    DtoTipoIncidente tipoCompleto = new DtoTipoIncidente(nombreTipo, null);
    
    // 2. Creamos el objeto incidente manualmente
    DtoIncidentes dtoFinal = new DtoIncidentes(
        null, 
        descripcion, 
        tipoCompleto, 
        (dtoEmpresa != null) ? dtoEmpresa.nombre().toUpperCase() : "ANONIMO",
        LocalDateTime.now(), 
        false, 
        region
    );

    servicioIncidentes.crearIncidentes(dtoFinal);
    return "redirect:/dashboard";
}

}