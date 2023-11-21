package eliminatorias.eliminatorias.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DetallesDTO {
    public PartidoDTO partidoDTO = new PartidoDTO();
    public DetallePartidoDTO detallePartidoDTO = new DetallePartidoDTO();
    public List<DetalleArbitroDTO> detalleArbitroDTO = new ArrayList<>();
    public List<DetalleTarjetaDTO> detalleTarjetaDTO = new ArrayList<>();
    public List<DetalleSustitucionDTO> detalleSustitucionDTO = new ArrayList<>();

    public Integer cantTarjetasLocal = 0;
    public Integer cantTarjetasVisitante = 0;
    public Integer cantSustitucionLocal = 0;
    public Integer cantSustitucionVisitante = 0;
}
