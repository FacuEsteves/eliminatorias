package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.DetalleArbitro;
import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.repos.DetalleArbitroRepository;
import eliminatorias.eliminatorias.repos.DetallePartidoRepository;
import eliminatorias.eliminatorias.repos.DetalleSustitucionRepository;
import eliminatorias.eliminatorias.repos.DetalleTarjetaRepository;
import eliminatorias.eliminatorias.repos.EstadioRepository;
import eliminatorias.eliminatorias.repos.JornadaRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final SeleccionRepository seleccionRepository;
    private final JornadaRepository jornadaRepository;
    private final EstadioRepository estadioRepository;
    private final DetallePartidoRepository detallePartidoRepository;
    private final DetalleTarjetaRepository detalleTarjetaRepository;
    private final DetalleSustitucionRepository detalleSustitucionRepository;
    private final DetalleArbitroRepository detalleArbitroRepository;

    public PartidoService(final PartidoRepository partidoRepository,
                          final SeleccionRepository seleccionRepository,
                          final JornadaRepository jornadaRepository, final EstadioRepository estadioRepository,
                          final DetallePartidoRepository detallePartidoRepository,
                          final DetalleTarjetaRepository detalleTarjetaRepository,
                          final DetalleSustitucionRepository detalleSustitucionRepository,
                          final DetalleArbitroRepository detalleArbitroRepository) {
        this.partidoRepository = partidoRepository;
        this.seleccionRepository = seleccionRepository;
        this.jornadaRepository = jornadaRepository;
        this.estadioRepository = estadioRepository;
        this.detallePartidoRepository = detallePartidoRepository;
        this.detalleTarjetaRepository = detalleTarjetaRepository;
        this.detalleSustitucionRepository = detalleSustitucionRepository;
        this.detalleArbitroRepository = detalleArbitroRepository;
    }

    public List<PartidoDTO> findAll() {
        final List<Partido> partidoes = partidoRepository.findAll(Sort.by("id"));
        return partidoes.stream()
                .map(partido -> mapToDTO(partido, new PartidoDTO()))
                .toList();
    }

    public PartidoDTO get(final Long id) {
        return partidoRepository.findById(id)
                .map(partido -> mapToDTO(partido, new PartidoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PartidoDTO partidoDTO) {
        final Partido partido = new Partido();
        mapToEntity(partidoDTO, partido);
        return partidoRepository.save(partido).getId();
    }

    public void update(final Long id, final PartidoDTO partidoDTO) {
        final Partido partido = partidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(partidoDTO, partido);
        partidoRepository.save(partido);
    }

    public void delete(final Long id) {
        partidoRepository.deleteById(id);
    }

    private PartidoDTO mapToDTO(final Partido partido, final PartidoDTO partidoDTO) {
        partidoDTO.setId(partido.getId());
        partidoDTO.setFecha(partido.getFecha());
        partidoDTO.setHoraLocal(partido.getHoraLocal());
        partidoDTO.setHoraGMT(partido.getHoraGMT());
        partidoDTO.setSeleccionLocal(partido.getSeleccionLocal() == null ? null : partido.getSeleccionLocal().getId());
        partidoDTO.setSeleccionLocalNombre(partido.getSeleccionLocal() == null ? null : partido.getSeleccionLocal().getPais().getNombre());
        partidoDTO.setSeleccionVisitante(partido.getSeleccionVisitante() == null ? null : partido.getSeleccionVisitante().getId());
        partidoDTO.setSeleccionVisitanteNombre(partido.getSeleccionVisitante() == null ? null : partido.getSeleccionVisitante().getPais().getNombre());
        partidoDTO.setJornada(partido.getJornada() == null ? null : partido.getJornada().getId());
        partidoDTO.setEstadio(partido.getEstadio() == null ? null : partido.getEstadio().getId());
        partidoDTO.setEstadioNombre(partido.getEstadio() == null ? null : partido.getEstadio().getNombre());
        return partidoDTO;
    }

    private Partido mapToEntity(final PartidoDTO partidoDTO, final Partido partido) {
        partido.setFecha(partidoDTO.getFecha());
        partido.setHoraLocal(partidoDTO.getHoraLocal());
        partido.setHoraGMT(partidoDTO.getHoraGMT());
        final Seleccion seleccionLocal = partidoDTO.getSeleccionLocal() == null ? null : seleccionRepository.findById(partidoDTO.getSeleccionLocal())
                .orElseThrow(() -> new NotFoundException("seleccionLocal not found"));
        partido.setSeleccionLocal(seleccionLocal);
        final Seleccion seleccionVisitante = partidoDTO.getSeleccionVisitante() == null ? null : seleccionRepository.findById(partidoDTO.getSeleccionVisitante())
                .orElseThrow(() -> new NotFoundException("seleccionVisitante not found"));
        partido.setSeleccionVisitante(seleccionVisitante);
        final Jornada jornada = partidoDTO.getJornada() == null ? null : jornadaRepository.findById(partidoDTO.getJornada())
                .orElseThrow(() -> new NotFoundException("jornada not found"));
        partido.setJornada(jornada);
        final Estadio estadio = partidoDTO.getEstadio() == null ? null : estadioRepository.findById(partidoDTO.getEstadio())
                .orElseThrow(() -> new NotFoundException("estadio not found"));
        partido.setEstadio(estadio);
        return partido;
    }

    public String getReferencedWarning(final Long id) {
        final Partido partido = partidoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final DetallePartido partidoDetallePartido = detallePartidoRepository.findFirstByPartidoId(partido.getId());
        if (partidoDetallePartido != null) {
            return WebUtils.getMessage("partido.detallePartido.partido.referenced", partidoDetallePartido.getId());
        }
        final DetalleTarjeta partidoDetalleTarjeta = detalleTarjetaRepository.findFirstByPartido(partido);
        if (partidoDetalleTarjeta != null) {
            return WebUtils.getMessage("partido.detalleTarjeta.partido.referenced", partidoDetalleTarjeta.getId());
        }
        final DetalleSustitucion partidoDetalleSustitucion = detalleSustitucionRepository.findFirstByPartido(partido);
        if (partidoDetalleSustitucion != null) {
            return WebUtils.getMessage("partido.detalleSustitucion.partido.referenced", partidoDetalleSustitucion.getId());
        }
        final DetalleArbitro partidoDetalleArbitro = detalleArbitroRepository.findFirstByPartido(partido);
        if (partidoDetalleArbitro != null) {
            return WebUtils.getMessage("partido.detalleArbitro.partido.referenced", partidoDetalleArbitro.getId());
        }
        return null;
    }

}