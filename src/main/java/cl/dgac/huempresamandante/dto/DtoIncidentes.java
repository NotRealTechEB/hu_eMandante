package cl.dgac.huempresamandante.dto;

import java.time.LocalDateTime;

public record DtoIncidentes(
    Long Id,
    String descripcion,
    DtoTipoIncidente tipo,
    String quien,
    LocalDateTime fecha_reporte,
    boolean resuelto,
    String region
) {

}
