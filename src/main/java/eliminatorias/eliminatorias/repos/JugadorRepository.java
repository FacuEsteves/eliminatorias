package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    Jugador findFirstBySeleccion(Seleccion seleccion);

    @Query(value = "SELECT jugadors.* FROM jugadors INNER JOIN partidoes ON jugadors.seleccion_id = partidoes.seleccion_local_id OR jugadors.seleccion_id = partidoes.seleccion_visitante_id WHERE partidoes.id = ?1 and jugadors.seleccion_id = ?2 ORDER BY jugadors.seleccion_id", nativeQuery = true)
    List<Jugador> obtenerJugadoresPorPartidoYSeleccion(Long idPartido, Long idSeleccion);

    @Query(value = "select * from jugadors where seleccion_id=?" , nativeQuery = true)
    List<Jugador> findJugadorSeleccion( Long id);

}
