package cl.dgac.huempresamandante.dto;

public record DtoSolicitudes(
    Long Id,
    String descripcion,
    double peso,
    String ubicacion,
    Boolean atendiada,
    String tipo,
    String rutEmpresaMandante,
    String rutEmpresaProveedora,
    String region
) {

}
