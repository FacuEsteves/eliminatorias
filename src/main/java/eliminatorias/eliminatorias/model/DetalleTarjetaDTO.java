package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleTarjetaDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String color;

    private Integer minuto;

    @NotNull
    private Long partido;

    @NotNull
    private Long jugador;
    private String jugadorNombre;
    private String jugadorPais;

}
