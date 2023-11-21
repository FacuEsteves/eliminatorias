package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EstadioDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    private Long capacidad;

    @NotNull
    private Long ciudad;


}
