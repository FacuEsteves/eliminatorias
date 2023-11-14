package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.DetalleArbitro;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DetalleArbitroRepository extends JpaRepository<DetalleArbitro, Long> {

    DetalleArbitro findFirstByPartido(Partido partido);

    @Query(value = "SELECT id,partido_id,arbitro_id,date_created,last_updated,tipo_arbitro_id FROM detalle_arbitroes WHERE partido_id = ?1 ORDER BY tipo_arbitro_id", nativeQuery = true)
    List<DetalleArbitro> findByPartidoId(Long idPartido);

    DetalleArbitro findFirstByArbitro(Arbitro arbitro);

    @Query(value = "SELECT COUNT(arbitro_id) FROM detalle_arbitroes WHERE partido_id = ?1 AND arbitro_id = ?2", nativeQuery = true)
    Integer buscarArbitroEnPartido(Long partidoId, Long arbitroId);

    @Query(value = "SELECT COUNT(tipo_arbitro_id) FROM detalle_arbitroes WHERE partido_id = ?1 AND tipo_arbitro_id = ?2", nativeQuery = true)
    Integer buscarTipoArbitroEnPartido(Long partidoId, Long tipoArbitroId);
}
