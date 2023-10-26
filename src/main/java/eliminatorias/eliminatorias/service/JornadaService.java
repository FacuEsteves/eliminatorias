package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.JornadaDTO;
import eliminatorias.eliminatorias.repos.JornadaRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class JornadaService {

    private final JornadaRepository jornadaRepository;
    private final PartidoRepository partidoRepository;

    public JornadaService(final JornadaRepository jornadaRepository,
            final PartidoRepository partidoRepository) {
        this.jornadaRepository = jornadaRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<JornadaDTO> findAll() {
        final List<Jornada> jornadas = jornadaRepository.findAll(Sort.by("id"));
        return jornadas.stream()
                .map(jornada -> mapToDTO(jornada, new JornadaDTO()))
                .toList();
    }

    public JornadaDTO get(final Long id) {
        return jornadaRepository.findById(id)
                .map(jornada -> mapToDTO(jornada, new JornadaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final JornadaDTO jornadaDTO) {
        final Jornada jornada = new Jornada();
        mapToEntity(jornadaDTO, jornada);
        return jornadaRepository.save(jornada).getId();
    }

    public void update(final Long id, final JornadaDTO jornadaDTO) {
        final Jornada jornada = jornadaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(jornadaDTO, jornada);
        jornadaRepository.save(jornada);
    }

    public void delete(final Long id) {
        jornadaRepository.deleteById(id);
    }

    private JornadaDTO mapToDTO(final Jornada jornada, final JornadaDTO jornadaDTO) {
        jornadaDTO.setId(jornada.getId());
        jornadaDTO.setFechaInicio(jornada.getFechaInicio());
        jornadaDTO.setFechaFin(jornada.getFechaFin());
        return jornadaDTO;
    }

    private Jornada mapToEntity(final JornadaDTO jornadaDTO, final Jornada jornada) {
        jornada.setFechaInicio(jornadaDTO.getFechaInicio());
        jornada.setFechaFin(jornadaDTO.getFechaFin());
        return jornada;
    }

    public String getReferencedWarning(final Long id) {
        final Jornada jornada = jornadaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Partido jornadaPartido = partidoRepository.findFirstByJornada(jornada);
        if (jornadaPartido != null) {
            return WebUtils.getMessage("jornada.partido.jornada.referenced", jornadaPartido.getId());
        }
        return null;
    }

}
