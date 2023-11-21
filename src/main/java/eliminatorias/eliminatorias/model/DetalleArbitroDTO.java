package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleArbitroDTO {

    private Long id;

    //@NotNull
    private Long partido;

    @NotNull
    private Long arbitro;
    private String arbitroNombre;

    @NotNull
    private Long tipoArbitro;
    private String tipoArbitroNombre;

}
