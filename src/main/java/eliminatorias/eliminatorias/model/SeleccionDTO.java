package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SeleccionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String sigla;

    private String escudo;

    @NotNull
    private Long pais;

}
