package eliminatorias.eliminatorias.model;

import jakarta.persistence.Lob;
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

    @Lob
    private byte[] escudo;

    @NotNull
    private Long pais;

}
