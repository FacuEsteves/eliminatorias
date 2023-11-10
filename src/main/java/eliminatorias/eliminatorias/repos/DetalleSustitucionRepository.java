package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetalleSustitucion;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetalleSustitucionRepository extends JpaRepository<DetalleSustitucion, Long> {

    DetalleSustitucion findFirstByPartido(Partido partido);

    List<DetalleSustitucion> findByPartidoId(Long idPartido);

    DetalleSustitucion findFirstByJugadorIngreso(Jugador jugador);

    DetalleSustitucion findFirstByJugadorEgreso(Jugador jugador);

}
