package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.TipoArbitro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TipoArbitroRepository extends JpaRepository<TipoArbitro, Long> {

    boolean existsByTipoIgnoreCase(String tipo);

}
