package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    Jugador findFirstBySeleccion(Seleccion seleccion);

}
