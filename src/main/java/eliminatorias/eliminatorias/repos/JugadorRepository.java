package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    Jugador findFirstBySeleccion(Seleccion seleccion);

    @Query(value = "select * from jugadors where seleccion_id=?" , nativeQuery = true)
    List<Jugador> findJugadorSeleccion( Long id);

}
