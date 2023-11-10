package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleSustitucionDTO {

    private Long id;

    @NotNull
    private Long partido;

    private String pais;

    @NotNull
    private Long jugadorIngreso;
    private String jugadorIngresoNombre;
    private Integer jugadorIngresoDorsal;
    private String jugadorIngresoPais;

    @NotNull
    private Long jugadorEgreso;
    private String jugadorEgresoNombre;
    private Integer jugadorEgresoDorsal;
    private String jugadorEgresoPais;
}
