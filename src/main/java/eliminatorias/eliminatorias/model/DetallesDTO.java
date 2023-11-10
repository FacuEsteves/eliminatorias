package eliminatorias.eliminatorias.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DetallesDTO {
    public String paisLocal;
    public String paisVisitante;
    public DetallePartidoDTO detallePartidoDTO = new DetallePartidoDTO();
    public List<DetalleArbitroDTO> detalleArbitroDTO = new ArrayList<>();
    public List<DetalleTarjetaDTO> detalleTarjetaDTO = new ArrayList<>();
    public List<DetalleSustitucionDTO> detalleSustitucionDTO = new ArrayList<>();
}
