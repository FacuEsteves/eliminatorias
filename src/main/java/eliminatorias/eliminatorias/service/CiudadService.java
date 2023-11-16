package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.CiudadDTO;
import eliminatorias.eliminatorias.repos.CiudadRepository;
import eliminatorias.eliminatorias.repos.EstadioRepository;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
//HOlaaaaaaaaaaaaaa!

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;
    private final PaisRepository paisRepository;
    private final EstadioRepository estadioRepository;

    public CiudadService(final CiudadRepository ciudadRepository,
            final PaisRepository paisRepository, final EstadioRepository estadioRepository) {
        this.ciudadRepository = ciudadRepository;
        this.paisRepository = paisRepository;
        this.estadioRepository = estadioRepository;
    }

    public List<CiudadDTO> findAll() {
        final List<Ciudad> ciudads = ciudadRepository.findAll(Sort.by("id"));
        return ciudads.stream()
                .map(ciudad -> mapToDTO(ciudad, new CiudadDTO()))
                .toList();
    }

    public CiudadDTO get(final Long id) {
        return ciudadRepository.findById(id)
                .map(ciudad -> mapToDTO(ciudad, new CiudadDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CiudadDTO ciudadDTO) {
        final Ciudad ciudad = new Ciudad();
        mapToEntity(ciudadDTO, ciudad);
        return ciudadRepository.save(ciudad).getId();
    }

    public void update(final Long id, final CiudadDTO ciudadDTO) {
        final Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ciudadDTO, ciudad);
        ciudadRepository.save(ciudad);
    }

    public void delete(final Long id) {
        ciudadRepository.deleteById(id);
    }

    private CiudadDTO mapToDTO(final Ciudad ciudad, final CiudadDTO ciudadDTO) {
        ciudadDTO.setId(ciudad.getId());
        ciudadDTO.setNombre(ciudad.getNombre());
        ciudadDTO.setZonaHoraria(ciudad.getZonaHoraria());
        ciudadDTO.setPais(ciudad.getPais() == null ? null : ciudad.getPais().getId());
        //ciudadDTO.setNombrePais(ciudad.getPais()==null?null:ciudad.getPais().getNombre());
        return ciudadDTO;
    }

    private Ciudad mapToEntity(final CiudadDTO ciudadDTO, final Ciudad ciudad) {
        ciudad.setNombre(ciudadDTO.getNombre());
        ciudad.setZonaHoraria(ciudadDTO.getZonaHoraria());
        final Pais pais = ciudadDTO.getPais() == null ? null : paisRepository.findById(ciudadDTO.getPais())
                .orElseThrow(() -> new NotFoundException("pais not found"));
        ciudad.setPais(pais);
        return ciudad;
    }

    public boolean nombreExists(final String nombre) {
        return ciudadRepository.existsByNombreIgnoreCase(nombre);
    }

    public String getReferencedWarning(final Long id) {
        final Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Estadio ciudadEstadio = estadioRepository.findFirstByCiudad(ciudad);
        if (ciudadEstadio != null) {
            return WebUtils.getMessage("ciudad.estadio.ciudad.referenced", ciudadEstadio.getId());
        }
        return null;
    }

}
