package cl.dgac.huempresamandante.dto;

import java.time.LocalDateTime;

public record DtoPlandeVuelo(
    String codigoVuelo,
    String rutEmpresaMandante,
    String numeroRegistro,
    LocalDateTime fechaPV,
    String psGPS,
    double altMax,
    String region,
    String estadoPV,
    DtoPiloto pilotoDTO
) {

}
