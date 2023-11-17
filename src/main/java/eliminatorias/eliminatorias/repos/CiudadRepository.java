package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    Ciudad findFirstByPais(Pais pais);

    boolean existsByNombreIgnoreCase(String nombre);

    @Query(value = "select * from ciudads where pais_id=?" , nativeQuery = true)
    List<Ciudad> findCiudadPais( Long id);
}
