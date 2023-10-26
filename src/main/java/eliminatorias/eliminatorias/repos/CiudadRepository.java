package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    Ciudad findFirstByPais(Pais pais);

    boolean existsByNombreIgnoreCase(String nombre);

}
