package eliminatorias.eliminatorias.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JornadaDTO {

    private Long id;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    private List<PartidoDTO> partidos = new ArrayList<>();

}
