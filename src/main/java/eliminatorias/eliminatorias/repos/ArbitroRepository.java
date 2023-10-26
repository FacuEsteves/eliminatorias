package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {

    Arbitro findFirstByPais(Pais pais);

    boolean existsByPaisId(Long id);

}
