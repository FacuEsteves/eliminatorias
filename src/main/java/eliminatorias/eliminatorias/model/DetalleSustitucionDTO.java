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

    @NotNull
    private Long jugadorIngreso;

    @NotNull
    private Long jugadorEgreso;

}
