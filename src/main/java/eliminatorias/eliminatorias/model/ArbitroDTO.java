package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ArbitroDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombreCompleto;

    @NotNull
    private Long pais;
    private String nombrePais;
}
