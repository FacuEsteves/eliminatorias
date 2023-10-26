package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetalleSustitucionRepository extends JpaRepository<DetalleSustitucion, Long> {

    DetalleSustitucion findFirstByDetallePartido(DetallePartido detallePartido);

    DetalleSustitucion findFirstByJugadorIngreso(Jugador jugador);

    DetalleSustitucion findFirstByJugadorEgreso(Jugador jugador);

}
