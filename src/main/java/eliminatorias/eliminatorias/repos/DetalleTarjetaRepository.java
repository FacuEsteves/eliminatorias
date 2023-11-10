package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetalleTarjetaRepository extends JpaRepository<DetalleTarjeta, Long> {

    DetalleTarjeta findFirstByPartido(Partido partido);

    List<DetalleTarjeta> findByPartidoId(Long idPartido);

    DetalleTarjeta findFirstByJugador(Jugador jugador);

}
