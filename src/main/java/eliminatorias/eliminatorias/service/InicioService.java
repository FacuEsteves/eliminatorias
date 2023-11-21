package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.model.InicioDTO;
import eliminatorias.eliminatorias.model.JornadaDTO;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.repos.JornadaRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InicioService {
    private final JornadaService jornadaService;
    private final PartidoService partidoService;

    public InicioService(JornadaService jornadaService, PartidoService partidoService) {
        this.jornadaService = jornadaService;
        this.partidoService = partidoService;
    }

    public InicioDTO partidosPorJornada(){
        final List<JornadaDTO> jornadas = jornadaService.findAll();
        final List<PartidoDTO> partidos = partidoService.findAll();
        return mapToDTO(jornadas,partidos,new InicioDTO());
    }

    private InicioDTO mapToDTO(final List<JornadaDTO> jornadas,
                               final List<PartidoDTO> partidos,
                               InicioDTO inicio){

        inicio.setPartidos(partidos);
        return inicio;
    }
}
