package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.JugadorDTO;
import eliminatorias.eliminatorias.repos.DetalleSustitucionRepository;
import eliminatorias.eliminatorias.repos.DetalleTarjetaRepository;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final SeleccionRepository seleccionRepository;
    private final DetalleTarjetaRepository detalleTarjetaRepository;
    private final DetalleSustitucionRepository detalleSustitucionRepository;

    public JugadorService(final JugadorRepository jugadorRepository,
            final SeleccionRepository seleccionRepository,
            final DetalleTarjetaRepository detalleTarjetaRepository,
            final DetalleSustitucionRepository detalleSustitucionRepository) {
        this.jugadorRepository = jugadorRepository;
        this.seleccionRepository = seleccionRepository;
        this.detalleTarjetaRepository = detalleTarjetaRepository;
        this.detalleSustitucionRepository = detalleSustitucionRepository;
    }

    public List<JugadorDTO> findAll() {
        final List<Jugador> jugadors = jugadorRepository.findAll(Sort.by("id"));
        return jugadors.stream()
                .map(jugador -> mapToDTO(jugador, new JugadorDTO()))
                .toList();
    }

    public JugadorDTO get(final Long id) {
        return jugadorRepository.findById(id)
                .map(jugador -> mapToDTO(jugador, new JugadorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final JugadorDTO jugadorDTO) {
        final Jugador jugador = new Jugador();
        mapToEntity(jugadorDTO, jugador);
        return jugadorRepository.save(jugador).getId();
    }

    public void update(final Long id, final JugadorDTO jugadorDTO) {
        final Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(jugadorDTO, jugador);
        jugadorRepository.save(jugador);
    }

    public void delete(final Long id) {
        jugadorRepository.deleteById(id);
    }

    private JugadorDTO mapToDTO(final Jugador jugador, final JugadorDTO jugadorDTO) {
        jugadorDTO.setId(jugador.getId());
        jugadorDTO.setNombreCompleto(jugador.getNombreCompleto());
        jugadorDTO.setEdad(jugador.getEdad());
        jugadorDTO.setPosicion(jugador.getPosicion());
        jugadorDTO.setDorsal(jugador.getDorsal());
        jugadorDTO.setSeleccion(jugador.getSeleccion() == null ? null : jugador.getSeleccion().getId());
        return jugadorDTO;
    }

    private Jugador mapToEntity(final JugadorDTO jugadorDTO, final Jugador jugador) {
        jugador.setNombreCompleto(jugadorDTO.getNombreCompleto());
        jugador.setEdad(jugadorDTO.getEdad());
        jugador.setPosicion(jugadorDTO.getPosicion());
        jugador.setDorsal(jugadorDTO.getDorsal());
        final Seleccion seleccion = jugadorDTO.getSeleccion() == null ? null : seleccionRepository.findById(jugadorDTO.getSeleccion())
                .orElseThrow(() -> new NotFoundException("seleccion not found"));
        jugador.setSeleccion(seleccion);
        return jugador;
    }

    public String getReferencedWarning(final Long id) {
        final Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final DetalleTarjeta jugadorDetalleTarjeta = detalleTarjetaRepository.findFirstByJugador(jugador);
        if (jugadorDetalleTarjeta != null) {
            return WebUtils.getMessage("jugador.detalleTarjeta.jugador.referenced", jugadorDetalleTarjeta.getId());
        }
        final DetalleSustitucion jugadorIngresoDetalleSustitucion = detalleSustitucionRepository.findFirstByJugadorIngreso(jugador);
        if (jugadorIngresoDetalleSustitucion != null) {
            return WebUtils.getMessage("jugador.detalleSustitucion.jugadorIngreso.referenced", jugadorIngresoDetalleSustitucion.getId());
        }
        final DetalleSustitucion jugadorEgresoDetalleSustitucion = detalleSustitucionRepository.findFirstByJugadorEgreso(jugador);
        if (jugadorEgresoDetalleSustitucion != null) {
            return WebUtils.getMessage("jugador.detalleSustitucion.jugadorEgreso.referenced", jugadorEgresoDetalleSustitucion.getId());
        }
        return null;
    }
}
