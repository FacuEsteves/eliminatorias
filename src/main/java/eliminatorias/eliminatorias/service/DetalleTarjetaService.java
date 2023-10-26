package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.model.DetalleTarjetaDTO;
import eliminatorias.eliminatorias.repos.DetallePartidoRepository;
import eliminatorias.eliminatorias.repos.DetalleTarjetaRepository;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DetalleTarjetaService {

    private final DetalleTarjetaRepository detalleTarjetaRepository;
    private final DetallePartidoRepository detallePartidoRepository;
    private final JugadorRepository jugadorRepository;

    public DetalleTarjetaService(final DetalleTarjetaRepository detalleTarjetaRepository,
            final DetallePartidoRepository detallePartidoRepository,
            final JugadorRepository jugadorRepository) {
        this.detalleTarjetaRepository = detalleTarjetaRepository;
        this.detallePartidoRepository = detallePartidoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    public List<DetalleTarjetaDTO> findAll() {
        final List<DetalleTarjeta> detalleTarjetas = detalleTarjetaRepository.findAll(Sort.by("id"));
        return detalleTarjetas.stream()
                .map(detalleTarjeta -> mapToDTO(detalleTarjeta, new DetalleTarjetaDTO()))
                .toList();
    }

    public DetalleTarjetaDTO get(final Long id) {
        return detalleTarjetaRepository.findById(id)
                .map(detalleTarjeta -> mapToDTO(detalleTarjeta, new DetalleTarjetaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DetalleTarjetaDTO detalleTarjetaDTO) {
        final DetalleTarjeta detalleTarjeta = new DetalleTarjeta();
        mapToEntity(detalleTarjetaDTO, detalleTarjeta);
        return detalleTarjetaRepository.save(detalleTarjeta).getId();
    }

    public void update(final Long id, final DetalleTarjetaDTO detalleTarjetaDTO) {
        final DetalleTarjeta detalleTarjeta = detalleTarjetaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(detalleTarjetaDTO, detalleTarjeta);
        detalleTarjetaRepository.save(detalleTarjeta);
    }

    public void delete(final Long id) {
        detalleTarjetaRepository.deleteById(id);
    }

    private DetalleTarjetaDTO mapToDTO(final DetalleTarjeta detalleTarjeta,
            final DetalleTarjetaDTO detalleTarjetaDTO) {
        detalleTarjetaDTO.setId(detalleTarjeta.getId());
        detalleTarjetaDTO.setColor(detalleTarjeta.getColor());
        detalleTarjetaDTO.setMinuto(detalleTarjeta.getMinuto());
        detalleTarjetaDTO.setDetallePartido(detalleTarjeta.getDetallePartido() == null ? null : detalleTarjeta.getDetallePartido().getId());
        detalleTarjetaDTO.setJugador(detalleTarjeta.getJugador() == null ? null : detalleTarjeta.getJugador().getId());
        return detalleTarjetaDTO;
    }

    private DetalleTarjeta mapToEntity(final DetalleTarjetaDTO detalleTarjetaDTO,
            final DetalleTarjeta detalleTarjeta) {
        detalleTarjeta.setColor(detalleTarjetaDTO.getColor());
        detalleTarjeta.setMinuto(detalleTarjetaDTO.getMinuto());
        final DetallePartido detallePartido = detalleTarjetaDTO.getDetallePartido() == null ? null : detallePartidoRepository.findById(detalleTarjetaDTO.getDetallePartido())
                .orElseThrow(() -> new NotFoundException("detallePartido not found"));
        detalleTarjeta.setDetallePartido(detallePartido);
        final Jugador jugador = detalleTarjetaDTO.getJugador() == null ? null : jugadorRepository.findById(detalleTarjetaDTO.getJugador())
                .orElseThrow(() -> new NotFoundException("jugador not found"));
        detalleTarjeta.setJugador(jugador);
        return detalleTarjeta;
    }

}
