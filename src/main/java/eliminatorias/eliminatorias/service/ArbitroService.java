package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.DetalleArbitro;
import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.ArbitroDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.DetalleArbitroRepository;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import eliminatorias.eliminatorias.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final PaisRepository paisRepository;
    private final DetalleArbitroRepository detalleArbitroRepository;

    public ArbitroService(final ArbitroRepository arbitroRepository,
            final PaisRepository paisRepository,
            final DetalleArbitroRepository detalleArbitroRepository) {
        this.arbitroRepository = arbitroRepository;
        this.paisRepository = paisRepository;
        this.detalleArbitroRepository = detalleArbitroRepository;
    }

    public List<ArbitroDTO> findAll() {
        final List<Arbitro> arbitroes = arbitroRepository.findAll(Sort.by("id"));
        return arbitroes.stream()
                .map(arbitro -> mapToDTO(arbitro, new ArbitroDTO()))
                .toList();
    }

    public ArbitroDTO get(final Long id) {
        return arbitroRepository.findById(id)
                .map(arbitro -> mapToDTO(arbitro, new ArbitroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ArbitroDTO arbitroDTO) {
        final Arbitro arbitro = new Arbitro();
        mapToEntity(arbitroDTO, arbitro);
        return arbitroRepository.save(arbitro).getId();
    }

    public void update(final Long id, final ArbitroDTO arbitroDTO) {
        final Arbitro arbitro = arbitroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(arbitroDTO, arbitro);
        arbitroRepository.save(arbitro);
    }

    public void delete(final Long id) {
        arbitroRepository.deleteById(id);
    }

    private ArbitroDTO mapToDTO(final Arbitro arbitro, final ArbitroDTO arbitroDTO) {
        arbitroDTO.setId(arbitro.getId());
        arbitroDTO.setNombreCompleto(arbitro.getNombreCompleto());
        arbitroDTO.setPais(arbitro.getPais() == null ? null : arbitro.getPais().getId());
        arbitroDTO.setNombrePais(arbitro.getPais() == null ? null : arbitro.getPais().getNombre());
        return arbitroDTO;
    }

    private Arbitro mapToEntity(final ArbitroDTO arbitroDTO, final Arbitro arbitro) {
        arbitro.setNombreCompleto(arbitroDTO.getNombreCompleto());
        final Pais pais = arbitroDTO.getPais() == null ? null : paisRepository.findById(arbitroDTO.getPais())
                .orElseThrow(() -> new NotFoundException("pais not found"));
        arbitro.setPais(pais);
        return arbitro;
    }

    public String getReferencedWarning(final Long id) {
        final Arbitro arbitro = arbitroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final DetalleArbitro arbitroDetalleArbitro = detalleArbitroRepository.findFirstByArbitro(arbitro);
        if (arbitroDetalleArbitro != null) {
            return WebUtils.getMessage("arbitro.detalleArbitro.arbitro.referenced", arbitroDetalleArbitro.getId());
        }
        return null;
    }

}
