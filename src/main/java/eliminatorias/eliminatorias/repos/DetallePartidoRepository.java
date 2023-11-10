package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface DetallePartidoRepository extends JpaRepository<DetallePartido, Long> {

    DetallePartido findFirstByPartidoId(Long idPartido);

    boolean existsByPartidoId(Long id);

}
