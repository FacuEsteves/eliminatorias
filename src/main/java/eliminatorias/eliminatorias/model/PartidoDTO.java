package eliminatorias.eliminatorias.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PartidoDTO {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Schema(type = "string", example = "18:30")
    private LocalTime horaLocal;

    @Schema(type = "string", example = "18:30")
    private LocalTime horaGMT;

    @NotNull
    private Long seleccionLocal;
    private String seleccionLocalNombre;
    private Integer seleccionLocalGoles;
    private String seleccionVisitantePais;
    private String seleccionLocalPais;


    @NotNull
    private Long seleccionVisitante;
    private String seleccionVisitanteNombre;
    private Integer seleccionVisitanteGoles;

    @NotNull
    private Long jornada;

    @NotNull
    private Long estadio;
    private String estadioNombre;

}
