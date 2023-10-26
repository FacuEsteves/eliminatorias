package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetallePartidoRepository extends JpaRepository<DetallePartido, Long> {

    DetallePartido findFirstByPartido(Partido partido);

    boolean existsByPartidoId(Long id);

}
