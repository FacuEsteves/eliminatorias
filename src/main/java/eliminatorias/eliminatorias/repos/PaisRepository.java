package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaisRepository extends JpaRepository<Pais, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByAbreviacionIgnoreCase(String abreviacion);

}
