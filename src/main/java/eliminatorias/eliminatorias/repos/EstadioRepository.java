package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Estadio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Estadio findFirstByCiudad(Ciudad ciudad);

    boolean existsByNombreIgnoreCase(String nombre);

    @Query(value = "select * from estadios where ciudad_id=?" , nativeQuery = true)
    List<Estadio> findEstadioCiudad( Long id);
}
