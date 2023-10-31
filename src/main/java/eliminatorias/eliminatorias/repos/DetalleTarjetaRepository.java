package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetalleTarjetaRepository extends JpaRepository<DetalleTarjeta, Long> {

    DetalleTarjeta findFirstByPartido(Partido partido);

    DetalleTarjeta findFirstByJugador(Jugador jugador);

}
