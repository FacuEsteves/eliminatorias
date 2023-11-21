package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.DetalleTarjeta;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DetalleTarjetaRepository extends JpaRepository<DetalleTarjeta, Long> {

    DetalleTarjeta findFirstByPartido(Partido partido);
    @Query(value = "SELECT id, partido_id, jugador_id, color, minuto, date_created, last_updated FROM detalle_tarjetas where partido_id = ?1 ORDER BY minuto", nativeQuery = true)
    List<DetalleTarjeta> findByPartidoId(Long idPartido);

    DetalleTarjeta findFirstByJugador(Jugador jugador);

}
