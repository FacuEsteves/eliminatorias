package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetalleTarjetaRepository extends JpaRepository<DetalleTarjeta, Long> {

    DetalleTarjeta findFirstByDetallePartido(DetallePartido detallePartido);

    DetalleTarjeta findFirstByJugador(Jugador jugador);

}
