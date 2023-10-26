package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.ArbitroDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final PaisRepository paisRepository;

    public ArbitroService(final ArbitroRepository arbitroRepository,
            final PaisRepository paisRepository) {
        this.arbitroRepository = arbitroRepository;
        this.paisRepository = paisRepository;
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
        arbitroDTO.setNombre(arbitro.getNombre());
        arbitroDTO.setApellido(arbitro.getApellido());
        arbitroDTO.setTipo(arbitro.getTipo());
        arbitroDTO.setPais(arbitro.getPais() == null ? null : arbitro.getPais().getId());
        return arbitroDTO;
    }

    private Arbitro mapToEntity(final ArbitroDTO arbitroDTO, final Arbitro arbitro) {
        arbitro.setNombre(arbitroDTO.getNombre());
        arbitro.setApellido(arbitroDTO.getApellido());
        arbitro.setTipo(arbitroDTO.getTipo());
        final Pais pais = arbitroDTO.getPais() == null ? null : paisRepository.findById(arbitroDTO.getPais())
                .orElseThrow(() -> new NotFoundException("pais not found"));
        arbitro.setPais(pais);
        return arbitro;
    }

    public boolean paisExists(final Long id) {
        return arbitroRepository.existsByPaisId(id);
    }

}
