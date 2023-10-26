package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SeleccionRepository extends JpaRepository<Seleccion, Long> {

    Seleccion findFirstByPais(Pais pais);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByPaisId(Long id);

}
