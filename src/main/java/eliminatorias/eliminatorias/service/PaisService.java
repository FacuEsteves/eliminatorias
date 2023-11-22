package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.PaisDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.CiudadRepository;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaisService {

    private final PaisRepository paisRepository;
    private final CiudadRepository ciudadRepository;
    private final SeleccionRepository seleccionRepository;
    private final ArbitroRepository arbitroRepository;

    public PaisService(final PaisRepository paisRepository, final CiudadRepository ciudadRepository,
            final SeleccionRepository seleccionRepository,
            final ArbitroRepository arbitroRepository) {
        this.paisRepository = paisRepository;
        this.ciudadRepository = ciudadRepository;
        this.seleccionRepository = seleccionRepository;
        this.arbitroRepository = arbitroRepository;
    }

    public List<PaisDTO> findAll() {
        final List<Pais> paises = paisRepository.findAll(Sort.by("id"));
        return paises.stream()
                .map(pais -> mapToDTO(pais, new PaisDTO()))
                .toList();
    }

    public PaisDTO get(final Long id) {
        return paisRepository.findById(id)
                .map(pais -> mapToDTO(pais, new PaisDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaisDTO paisDTO) {
        final Pais pais = new Pais();
        mapToEntity(paisDTO, pais);
        return paisRepository.save(pais).getId();
    }

    public void update(final Long id, final PaisDTO paisDTO) {
        final Pais pais = paisRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paisDTO, pais);
        paisRepository.save(pais);
    }

    public void delete(final Long id) {
        paisRepository.deleteById(id);
    }

    private PaisDTO mapToDTO(final Pais pais, final PaisDTO paisDTO) {
        paisDTO.setId(pais.getId());
        paisDTO.setNombre(pais.getNombre());
        paisDTO.setAbreviacion(pais.getAbreviacion());
        paisDTO.setBandera(paisDTO.getBandera());
        return paisDTO;
    }

    private Pais mapToEntity(final PaisDTO paisDTO, final Pais pais) {
        pais.setNombre(paisDTO.getNombre());
        pais.setAbreviacion(paisDTO.getAbreviacion());
        pais.setBandera(paisDTO.getBandera());
        return pais;
    }

    public byte[] obtenerImagen(Long id) {
        Optional<Pais> optionalPais = paisRepository.findById(id);

        return optionalPais.map(Pais::getBandera).orElse(null);
    }

    public boolean nombreExists(final String nombre) {
        return paisRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean abreviacionExists(final String abreviacion) {
        return paisRepository.existsByAbreviacionIgnoreCase(abreviacion);
    }

    public String getReferencedWarning(final Long id) {
        final Pais pais = paisRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ciudad paisCiudad = ciudadRepository.findFirstByPais(pais);
        if (paisCiudad != null) {
            return WebUtils.getMessage("pais.ciudad.pais.referenced", paisCiudad.getId());
        }
        final Seleccion paisSeleccion = seleccionRepository.findFirstByPais(pais);
        if (paisSeleccion != null) {
            return WebUtils.getMessage("pais.seleccion.pais.referenced", paisSeleccion.getId());
        }
        final Arbitro paisArbitro = arbitroRepository.findFirstByPais(pais);
        if (paisArbitro != null) {
            return WebUtils.getMessage("pais.arbitro.pais.referenced", paisArbitro.getId());
        }
        return null;
    }

}
