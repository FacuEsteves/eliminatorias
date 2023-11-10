package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.DetalleArbitro;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetalleArbitroRepository extends JpaRepository<DetalleArbitro, Long> {

    DetalleArbitro findFirstByPartido(Partido partido);

    List<DetalleArbitro> findByPartidoId(Long idPartido);

    DetalleArbitro findFirstByArbitro(Arbitro arbitro);

}
