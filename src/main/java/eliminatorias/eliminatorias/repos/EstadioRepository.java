package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Estadio findFirstByCiudad(Ciudad ciudad);

    boolean existsByNombreIgnoreCase(String nombre);

}
