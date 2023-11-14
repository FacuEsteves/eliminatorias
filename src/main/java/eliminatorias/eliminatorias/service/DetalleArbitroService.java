package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.DetalleArbitro;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.TipoArbitro;
import eliminatorias.eliminatorias.model.DetalleArbitroDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.DetalleArbitroRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.repos.TipoArbitroRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DetalleArbitroService {

    private final DetalleArbitroRepository detalleArbitroRepository;
    private final PartidoRepository partidoRepository;
    private final ArbitroRepository arbitroRepository;
    private final TipoArbitroRepository tipoArbitroRepository;

    public DetalleArbitroService(final DetalleArbitroRepository detalleArbitroRepository,
                                 final PartidoRepository partidoRepository, final ArbitroRepository arbitroRepository,
                                 final TipoArbitroRepository tipoArbitroRepository) {
        this.detalleArbitroRepository = detalleArbitroRepository;
        this.partidoRepository = partidoRepository;
        this.arbitroRepository = arbitroRepository;
        this.tipoArbitroRepository = tipoArbitroRepository;
    }

    public List<DetalleArbitroDTO> findAll() {
        final List<DetalleArbitro> detalleArbitroes = detalleArbitroRepository.findAll(Sort.by("id"));
        return detalleArbitroes.stream()
                .map(detalleArbitro -> mapToDTO(detalleArbitro, new DetalleArbitroDTO()))
                .toList();
    }

    public DetalleArbitroDTO get(final Long id) {
        return detalleArbitroRepository.findById(id)
                .map(detalleArbitro -> mapToDTO(detalleArbitro, new DetalleArbitroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DetalleArbitroDTO detalleArbitroDTO) {
        final DetalleArbitro detalleArbitro = new DetalleArbitro();
        mapToEntity(detalleArbitroDTO, detalleArbitro);
        return detalleArbitroRepository.save(detalleArbitro).getId();
    }

    public void update(final Long id, final DetalleArbitroDTO detalleArbitroDTO) {
        final DetalleArbitro detalleArbitro = detalleArbitroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(detalleArbitroDTO, detalleArbitro);
        detalleArbitroRepository.save(detalleArbitro);
    }

    public void delete(final Long id) {
        detalleArbitroRepository.deleteById(id);
    }

    public DetalleArbitroDTO mapToDTO(final DetalleArbitro detalleArbitro,
                                       final DetalleArbitroDTO detalleArbitroDTO) {
        detalleArbitroDTO.setId(detalleArbitro.getId());
        detalleArbitroDTO.setPartido(detalleArbitro.getPartido() == null ? null : detalleArbitro.getPartido().getId());
        detalleArbitroDTO.setArbitro(detalleArbitro.getArbitro() == null ? null : detalleArbitro.getArbitro().getId());
        detalleArbitroDTO.setArbitroNombre(detalleArbitro.getArbitro() == null ? null : detalleArbitro.getArbitro().getNombreCompleto());
        detalleArbitroDTO.setTipoArbitro(detalleArbitro.getTipoArbitro() == null ? null : detalleArbitro.getTipoArbitro().getId());
        detalleArbitroDTO.setTipoArbitroNombre(detalleArbitro.getTipoArbitro() == null ? null : detalleArbitro.getTipoArbitro().getTipo());
        return detalleArbitroDTO;
    }

    private DetalleArbitro mapToEntity(final DetalleArbitroDTO detalleArbitroDTO,
                                       final DetalleArbitro detalleArbitro) {
        final Partido partido = detalleArbitroDTO.getPartido() == null ? null : partidoRepository.findById(detalleArbitroDTO.getPartido())
                .orElseThrow(() -> new NotFoundException("partido not found"));
        detalleArbitro.setPartido(partido);
        final Arbitro arbitro = detalleArbitroDTO.getArbitro() == null ? null : arbitroRepository.findById(detalleArbitroDTO.getArbitro())
                .orElseThrow(() -> new NotFoundException("arbitro not found"));
        detalleArbitro.setArbitro(arbitro);
        final TipoArbitro tipoArbitro = detalleArbitroDTO.getTipoArbitro() == null ? null : tipoArbitroRepository.findById(detalleArbitroDTO.getTipoArbitro())
                .orElseThrow(() -> new NotFoundException("tipoArbitro not found"));
        detalleArbitro.setTipoArbitro(tipoArbitro);
        return detalleArbitro;
    }
}
