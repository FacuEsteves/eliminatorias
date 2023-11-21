package eliminatorias.eliminatorias.service;

import eliminatorias.eliminatorias.domain.TipoArbitro;
import eliminatorias.eliminatorias.model.TipoArbitroDTO;
import eliminatorias.eliminatorias.repos.TipoArbitroRepository;
import eliminatorias.eliminatorias.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TipoArbitroService {

    private final TipoArbitroRepository tipoArbitroRepository;

    public TipoArbitroService(final TipoArbitroRepository tipoArbitroRepository) {
        this.tipoArbitroRepository = tipoArbitroRepository;
    }

    public List<TipoArbitroDTO> findAll() {
        final List<TipoArbitro> tipoArbitroes = tipoArbitroRepository.findAll(Sort.by("id"));
        return tipoArbitroes.stream()
                .map(tipoArbitro -> mapToDTO(tipoArbitro, new TipoArbitroDTO()))
                .toList();
    }

    public TipoArbitroDTO get(final Long id) {
        return tipoArbitroRepository.findById(id)
                .map(tipoArbitro -> mapToDTO(tipoArbitro, new TipoArbitroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TipoArbitroDTO tipoArbitroDTO) {
        final TipoArbitro tipoArbitro = new TipoArbitro();
        mapToEntity(tipoArbitroDTO, tipoArbitro);
        return tipoArbitroRepository.save(tipoArbitro).getId();
    }

    public void update(final Long id, final TipoArbitroDTO tipoArbitroDTO) {
        final TipoArbitro tipoArbitro = tipoArbitroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tipoArbitroDTO, tipoArbitro);
        tipoArbitroRepository.save(tipoArbitro);
    }

    public void delete(final Long id) {
        tipoArbitroRepository.deleteById(id);
    }

    private TipoArbitroDTO mapToDTO(final TipoArbitro tipoArbitro,
            final TipoArbitroDTO tipoArbitroDTO) {
        tipoArbitroDTO.setId(tipoArbitro.getId());
        tipoArbitroDTO.setTipo(tipoArbitro.getTipo());
        return tipoArbitroDTO;
    }

    private TipoArbitro mapToEntity(final TipoArbitroDTO tipoArbitroDTO,
            final TipoArbitro tipoArbitro) {
        tipoArbitro.setTipo(tipoArbitroDTO.getTipo());
        return tipoArbitro;
    }

    public boolean tipoExists(final String tipo) {
        return tipoArbitroRepository.existsByTipoIgnoreCase(tipo);
    }

}
