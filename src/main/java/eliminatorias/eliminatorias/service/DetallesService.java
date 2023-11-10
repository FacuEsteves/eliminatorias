package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.controller.DetalleArbitroController;
import eliminatorias.eliminatorias.domain.*;
import eliminatorias.eliminatorias.model.*;
import eliminatorias.eliminatorias.repos.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallesService {

    private final PartidoRepository partidoRepository;
    private final DetallePartidoRepository detallePartidoRepository;
    private final DetalleArbitroRepository detalleArbitroRepository;
    private final DetalleArbitroService detalleArbitroService;
    private final DetalleSustitucionRepository detalleSustitucionRepository;
    private final DetalleSustitucionService detalleSustitucionService;
    private final DetalleTarjetaRepository detalleTarjetaRepository;
    private final DetalleTarjetaService detalleTarjetaService;

    public DetallesService(DetallePartidoRepository detallePartidoRepository, DetalleArbitroRepository detalleArbitroRepository, DetalleArbitroController detalleArbitroController, DetalleArbitroService detalleArbitroService, DetalleSustitucionRepository detalleSustitucionRepository, DetalleSustitucionService detalleSustitucionService, DetalleTarjetaRepository detalleTarjetaRepository, DetalleTarjetaService detalleTarjetaService, PartidoRepository partidoRepository, PartidoRepository partidoRepository1) {
        this.detallePartidoRepository = detallePartidoRepository;
        this.detalleArbitroRepository = detalleArbitroRepository;
        this.detalleArbitroService = detalleArbitroService;
        this.detalleSustitucionRepository = detalleSustitucionRepository;
        this.detalleSustitucionService = detalleSustitucionService;
        this.detalleTarjetaRepository = detalleTarjetaRepository;
        this.detalleTarjetaService = detalleTarjetaService;
        this.partidoRepository = partidoRepository;
    }

    public DetallesDTO findByPartido(final Long idPartido){
        final Optional<Partido> partido = partidoRepository.findById(idPartido);
        final DetallePartido detallePartido = detallePartidoRepository.findFirstByPartidoId(idPartido);
        final List<DetalleArbitro> detallesArbitro = detalleArbitroRepository.findByPartidoId(idPartido);
        final List<DetalleSustitucion> detallesSubstitution = detalleSustitucionRepository.findByPartidoId(idPartido);
        final List<DetalleTarjeta> detallesTarjeta = detalleTarjetaRepository.findByPartidoId(idPartido);
        return mapToDTO(partido, detallePartido, detallesArbitro, detallesSubstitution, detallesTarjeta, new DetallesDTO());
    }

    private DetallesDTO mapToDTO(final Optional<Partido> partido,
                                 final DetallePartido detallePartido,
                                 final List<DetalleArbitro> detallesArbitro,
                                 final List<DetalleSustitucion> detallesSubstitution,
                                 final List<DetalleTarjeta> detallesTarjeta,
                                 final DetallesDTO detallesDTO) {
        detallesDTO.setPaisLocal(partido.get().getSeleccionLocal().getPais().getNombre());
        detallesDTO.setPaisVisitante(partido.get().getSeleccionVisitante().getPais().getNombre());

        if(detallePartido != null){
            detallesDTO.detallePartidoDTO.setId(detallePartido.getId());
            detallesDTO.detallePartidoDTO.setGolLocal(detallePartido.getGolLocal());
            detallesDTO.detallePartidoDTO.setGolVisitante(detallePartido.getGolVisitante());
            detallesDTO.detallePartidoDTO.setPartido(detallePartido.getPartido() == null ? null : detallePartido.getPartido().getId());
        }
        if(!detallesArbitro.isEmpty()){
            detallesDTO.detalleArbitroDTO.addAll(detallesArbitro.stream()
                    .map(detalleArbitro -> detalleArbitroService.mapToDTO(detalleArbitro, new DetalleArbitroDTO()))
                    .toList());
        }
        if(!detallesSubstitution.isEmpty()){
            detallesDTO.detalleSustitucionDTO.addAll(detallesSubstitution.stream()
                    .map(detalleSustitucion -> detalleSustitucionService.mapToDTO(detalleSustitucion, new DetalleSustitucionDTO()))
                    .toList());
        }
        if(!detallesTarjeta.isEmpty()){
            detallesDTO.detalleTarjetaDTO.addAll(detallesTarjeta.stream()
                    .map(detalleTarjeta -> detalleTarjetaService.mapToDTO(detalleTarjeta, new DetalleTarjetaDTO()))
                    .toList());
        }
        return detallesDTO;
    }

}
