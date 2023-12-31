package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleSustitucionDTO {

    private Long id;

    private Long partido;

    private Long seleccion;

    @NotNull
    private Long jugadorIngreso;
    private String jugadorIngresoNombre;
    private Integer jugadorIngresoDorsal;

    @NotNull
    private Long jugadorEgreso;
    private String jugadorEgresoNombre;
    private Integer jugadorEgresoDorsal;
}
