package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleArbitroDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tipo;

    @NotNull
    private Long partido;

    @NotNull
    private Long arbitro;

}
