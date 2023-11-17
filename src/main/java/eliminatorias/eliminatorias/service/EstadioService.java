package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.CiudadDTO;
import eliminatorias.eliminatorias.model.EstadioDTO;
import eliminatorias.eliminatorias.repos.CiudadRepository;
import eliminatorias.eliminatorias.repos.EstadioRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;
    private final CiudadRepository ciudadRepository;
    private final PartidoRepository partidoRepository;

    public EstadioService(final EstadioRepository estadioRepository,
                          final CiudadRepository ciudadRepository, final PartidoRepository partidoRepository) {
        this.estadioRepository = estadioRepository;
        this.ciudadRepository = ciudadRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<EstadioDTO> findAll() {
        final List<Estadio> estadios = estadioRepository.findAll(Sort.by("id"));
        return estadios.stream()
                .map(estadio -> mapToDTO(estadio, new EstadioDTO()))
                .toList();
    }

    public List<EstadioDTO> findEstadioCiudad( Long id) {
        final List<Estadio> estadios = estadioRepository.findEstadioCiudad(id);
        return estadios.stream()
                .map(estadio -> mapToDTO(estadio, new EstadioDTO()))
                .toList();
    }

    public EstadioDTO get(final Long id) {
        return estadioRepository.findById(id)
                .map(estadio -> mapToDTO(estadio, new EstadioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EstadioDTO estadioDTO) {
        final Estadio estadio = new Estadio();
        mapToEntity(estadioDTO, estadio);
        return estadioRepository.save(estadio).getId();
    }

    public void update(final Long id, final EstadioDTO estadioDTO) {
        final Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(estadioDTO, estadio);
        estadioRepository.save(estadio);
    }

    public void delete(final Long id) {
        estadioRepository.deleteById(id);
    }

    private EstadioDTO mapToDTO(final Estadio estadio, final EstadioDTO estadioDTO) {
        estadioDTO.setId(estadio.getId());
        estadioDTO.setNombre(estadio.getNombre());
        estadioDTO.setCapacidad(estadio.getCapacidad());
        estadioDTO.setCiudad(estadio.getCiudad() == null ? null : estadio.getCiudad().getId());
        return estadioDTO;
    }

    private Estadio mapToEntity(final EstadioDTO estadioDTO, final Estadio estadio) {
        estadio.setNombre(estadioDTO.getNombre());
        estadio.setCapacidad(estadioDTO.getCapacidad());
        final Ciudad ciudad = estadioDTO.getCiudad() == null ? null : ciudadRepository.findById(estadioDTO.getCiudad())
                .orElseThrow(() -> new NotFoundException("ciudad not found"));
        estadio.setCiudad(ciudad);
        return estadio;
    }

    public boolean nombreExists(final String nombre) {
        return estadioRepository.existsByNombreIgnoreCase(nombre);
    }

    public String getReferencedWarning(final Long id) {
        final Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Partido estadioPartido = partidoRepository.findFirstByEstadio(estadio);
        if (estadioPartido != null) {
            return WebUtils.getMessage("estadio.partido.estadio.referenced", estadioPartido.getId());
        }
        return null;
    }

}
