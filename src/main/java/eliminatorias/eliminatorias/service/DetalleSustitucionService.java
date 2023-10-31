package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetalleSustitucionDTO;
import eliminatorias.eliminatorias.repos.DetalleSustitucionRepository;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DetalleSustitucionService {

    private final DetalleSustitucionRepository detalleSustitucionRepository;
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;

    public DetalleSustitucionService(
            final DetalleSustitucionRepository detalleSustitucionRepository,
            final PartidoRepository partidoRepository, final JugadorRepository jugadorRepository) {
        this.detalleSustitucionRepository = detalleSustitucionRepository;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    public List<DetalleSustitucionDTO> findAll() {
        final List<DetalleSustitucion> detalleSustitucions = detalleSustitucionRepository.findAll(Sort.by("id"));
        return detalleSustitucions.stream()
                .map(detalleSustitucion -> mapToDTO(detalleSustitucion, new DetalleSustitucionDTO()))
                .toList();
    }

    public DetalleSustitucionDTO get(final Long id) {
        return detalleSustitucionRepository.findById(id)
                .map(detalleSustitucion -> mapToDTO(detalleSustitucion, new DetalleSustitucionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DetalleSustitucionDTO detalleSustitucionDTO) {
        final DetalleSustitucion detalleSustitucion = new DetalleSustitucion();
        mapToEntity(detalleSustitucionDTO, detalleSustitucion);
        return detalleSustitucionRepository.save(detalleSustitucion).getId();
    }

    public void update(final Long id, final DetalleSustitucionDTO detalleSustitucionDTO) {
        final DetalleSustitucion detalleSustitucion = detalleSustitucionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(detalleSustitucionDTO, detalleSustitucion);
        detalleSustitucionRepository.save(detalleSustitucion);
    }

    public void delete(final Long id) {
        detalleSustitucionRepository.deleteById(id);
    }

    private DetalleSustitucionDTO mapToDTO(final DetalleSustitucion detalleSustitucion,
            final DetalleSustitucionDTO detalleSustitucionDTO) {
        detalleSustitucionDTO.setId(detalleSustitucion.getId());
        detalleSustitucionDTO.setPartido(detalleSustitucion.getPartido() == null ? null : detalleSustitucion.getPartido().getId());
        detalleSustitucionDTO.setJugadorIngreso(detalleSustitucion.getJugadorIngreso() == null ? null : detalleSustitucion.getJugadorIngreso().getId());
        detalleSustitucionDTO.setJugadorEgreso(detalleSustitucion.getJugadorEgreso() == null ? null : detalleSustitucion.getJugadorEgreso().getId());
        return detalleSustitucionDTO;
    }

    private DetalleSustitucion mapToEntity(final DetalleSustitucionDTO detalleSustitucionDTO,
            final DetalleSustitucion detalleSustitucion) {
        final Partido partido = detalleSustitucionDTO.getPartido() == null ? null : partidoRepository.findById(detalleSustitucionDTO.getPartido())
                .orElseThrow(() -> new NotFoundException("partido not found"));
        detalleSustitucion.setPartido(partido);
        final Jugador jugadorIngreso = detalleSustitucionDTO.getJugadorIngreso() == null ? null : jugadorRepository.findById(detalleSustitucionDTO.getJugadorIngreso())
                .orElseThrow(() -> new NotFoundException("jugadorIngreso not found"));
        detalleSustitucion.setJugadorIngreso(jugadorIngreso);
        final Jugador jugadorEgreso = detalleSustitucionDTO.getJugadorEgreso() == null ? null : jugadorRepository.findById(detalleSustitucionDTO.getJugadorEgreso())
                .orElseThrow(() -> new NotFoundException("jugadorEgreso not found"));
        detalleSustitucion.setJugadorEgreso(jugadorEgreso);
        return detalleSustitucion;
    }

}
