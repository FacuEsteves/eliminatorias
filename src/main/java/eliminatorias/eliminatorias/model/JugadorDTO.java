package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JugadorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombreCompleto;

    private Integer edad;

    @NotNull
    @Size(max = 255)
    private String posicion;

    private Integer dorsal;

    @NotNull
    private Long seleccion;

}
