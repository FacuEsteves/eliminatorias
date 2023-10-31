package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.SeleccionDTO;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SeleccionService {

    private final SeleccionRepository seleccionRepository;
    private final PaisRepository paisRepository;
    private final JugadorRepository jugadorRepository;
    private final PartidoRepository partidoRepository;

    public SeleccionService(final SeleccionRepository seleccionRepository,
            final PaisRepository paisRepository, final JugadorRepository jugadorRepository,
            final PartidoRepository partidoRepository) {
        this.seleccionRepository = seleccionRepository;
        this.paisRepository = paisRepository;
        this.jugadorRepository = jugadorRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<SeleccionDTO> findAll() {
        final List<Seleccion> seleccions = seleccionRepository.findAll(Sort.by("id"));
        return seleccions.stream()
                .map(seleccion -> mapToDTO(seleccion, new SeleccionDTO()))
                .toList();
    }

    public SeleccionDTO get(final Long id) {
        return seleccionRepository.findById(id)
                .map(seleccion -> mapToDTO(seleccion, new SeleccionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SeleccionDTO seleccionDTO) {
        final Seleccion seleccion = new Seleccion();
        mapToEntity(seleccionDTO, seleccion);
        return seleccionRepository.save(seleccion).getId();
    }

    public void update(final Long id, final SeleccionDTO seleccionDTO) {
        final Seleccion seleccion = seleccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(seleccionDTO, seleccion);
        seleccionRepository.save(seleccion);
    }

    public void delete(final Long id) {
        seleccionRepository.deleteById(id);
    }

    private SeleccionDTO mapToDTO(final Seleccion seleccion, final SeleccionDTO seleccionDTO) {
        seleccionDTO.setId(seleccion.getId());
        seleccionDTO.setNombre(seleccion.getNombre());
        seleccionDTO.setSigla(seleccion.getSigla());
        seleccionDTO.setEscudo(seleccion.getEscudo());
        seleccionDTO.setPais(seleccion.getPais() == null ? null : seleccion.getPais().getId());
        return seleccionDTO;
    }

    private Seleccion mapToEntity(final SeleccionDTO seleccionDTO, final Seleccion seleccion) {
        seleccion.setNombre(seleccionDTO.getNombre());
        seleccion.setSigla(seleccionDTO.getSigla());
        seleccion.setEscudo(seleccionDTO.getEscudo());
        final Pais pais = seleccionDTO.getPais() == null ? null : paisRepository.findById(seleccionDTO.getPais())
                .orElseThrow(() -> new NotFoundException("pais not found"));
        seleccion.setPais(pais);
        return seleccion;
    }

    public boolean nombreExists(final String nombre) {
        return seleccionRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean paisExists(final Long id) {
        return seleccionRepository.existsByPaisId(id);
    }

    public String getReferencedWarning(final Long id) {
        final Seleccion seleccion = seleccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Jugador seleccionJugador = jugadorRepository.findFirstBySeleccion(seleccion);
        if (seleccionJugador != null) {
            return WebUtils.getMessage("seleccion.jugador.seleccion.referenced", seleccionJugador.getId());
        }
        final Partido seleccionLocalPartido = partidoRepository.findFirstBySeleccionLocal(seleccion);
        if (seleccionLocalPartido != null) {
            return WebUtils.getMessage("seleccion.partido.seleccionLocal.referenced", seleccionLocalPartido.getId());
        }
        final Partido seleccionVisitantePartido = partidoRepository.findFirstBySeleccionVisitante(seleccion);
        if (seleccionVisitantePartido != null) {
            return WebUtils.getMessage("seleccion.partido.seleccionVisitante.referenced", seleccionVisitantePartido.getId());
        }
        return null;
    }

}
