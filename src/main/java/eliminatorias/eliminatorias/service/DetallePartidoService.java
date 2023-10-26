package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetallePartidoDTO;
import eliminatorias.eliminatorias.repos.DetallePartidoRepository;
import eliminatorias.eliminatorias.repos.DetalleSustitucionRepository;
import eliminatorias.eliminatorias.repos.DetalleTarjetaRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DetallePartidoService {

    private final DetallePartidoRepository detallePartidoRepository;
    private final PartidoRepository partidoRepository;
    private final DetalleTarjetaRepository detalleTarjetaRepository;
    private final DetalleSustitucionRepository detalleSustitucionRepository;

    public DetallePartidoService(final DetallePartidoRepository detallePartidoRepository,
            final PartidoRepository partidoRepository,
            final DetalleTarjetaRepository detalleTarjetaRepository,
            final DetalleSustitucionRepository detalleSustitucionRepository) {
        this.detallePartidoRepository = detallePartidoRepository;
        this.partidoRepository = partidoRepository;
        this.detalleTarjetaRepository = detalleTarjetaRepository;
        this.detalleSustitucionRepository = detalleSustitucionRepository;
    }

    public List<DetallePartidoDTO> findAll() {
        final List<DetallePartido> detallePartidoes = detallePartidoRepository.findAll(Sort.by("id"));
        return detallePartidoes.stream()
                .map(detallePartido -> mapToDTO(detallePartido, new DetallePartidoDTO()))
                .toList();
    }

    public DetallePartidoDTO get(final Long id) {
        return detallePartidoRepository.findById(id)
                .map(detallePartido -> mapToDTO(detallePartido, new DetallePartidoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DetallePartidoDTO detallePartidoDTO) {
        final DetallePartido detallePartido = new DetallePartido();
        mapToEntity(detallePartidoDTO, detallePartido);
        return detallePartidoRepository.save(detallePartido).getId();
    }

    public void update(final Long id, final DetallePartidoDTO detallePartidoDTO) {
        final DetallePartido detallePartido = detallePartidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(detallePartidoDTO, detallePartido);
        detallePartidoRepository.save(detallePartido);
    }

    public void delete(final Long id) {
        detallePartidoRepository.deleteById(id);
    }

    private DetallePartidoDTO mapToDTO(final DetallePartido detallePartido,
            final DetallePartidoDTO detallePartidoDTO) {
        detallePartidoDTO.setId(detallePartido.getId());
        detallePartidoDTO.setGolLocal(detallePartido.getGolLocal());
        detallePartidoDTO.setGolVisitante(detallePartido.getGolVisitante());
        detallePartidoDTO.setPartido(detallePartido.getPartido() == null ? null : detallePartido.getPartido().getId());
        return detallePartidoDTO;
    }

    private DetallePartido mapToEntity(final DetallePartidoDTO detallePartidoDTO,
            final DetallePartido detallePartido) {
        detallePartido.setGolLocal(detallePartidoDTO.getGolLocal());
        detallePartido.setGolVisitante(detallePartidoDTO.getGolVisitante());
        final Partido partido = detallePartidoDTO.getPartido() == null ? null : partidoRepository.findById(detallePartidoDTO.getPartido())
                .orElseThrow(() -> new NotFoundException("partido not found"));
        detallePartido.setPartido(partido);
        return detallePartido;
    }

    public boolean partidoExists(final Long id) {
        return detallePartidoRepository.existsByPartidoId(id);
    }

    public String getReferencedWarning(final Long id) {
        final DetallePartido detallePartido = detallePartidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final DetalleTarjeta detallePartidoDetalleTarjeta = detalleTarjetaRepository.findFirstByDetallePartido(detallePartido);
        if (detallePartidoDetalleTarjeta != null) {
            return WebUtils.getMessage("detallePartido.detalleTarjeta.detallePartido.referenced", detallePartidoDetalleTarjeta.getId());
        }
        final DetalleSustitucion detallePartidoDetalleSustitucion = detalleSustitucionRepository.findFirstByDetallePartido(detallePartido);
        if (detallePartidoDetalleSustitucion != null) {
            return WebUtils.getMessage("detallePartido.detalleSustitucion.detallePartido.referenced", detallePartidoDetalleSustitucion.getId());
        }
        return null;
    }

}
