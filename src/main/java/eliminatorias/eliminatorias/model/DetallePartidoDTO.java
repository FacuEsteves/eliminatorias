package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetallePartidoDTO {

    private Long id;

    @NotNull
    private Integer golLocal;

    @NotNull
    private Integer golVisitante;

    //@NotNull
    private Long partido;

    public boolean isEmpty(){
        // Si todos los campos obligatorios son nulos, entonces consideramos que el DTO está vacío
        return id == null && golLocal == null && golVisitante == null && partido == null;
    }
}
