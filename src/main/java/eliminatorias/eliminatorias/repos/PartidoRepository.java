package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PartidoRepository extends JpaRepository<Partido, Long> {

    Partido findFirstBySeleccionLocal(Seleccion seleccion);

    Partido findFirstBySeleccionVisitante(Seleccion seleccion);

    Partido findFirstByJornada(Jornada jornada);

    // Método utilizando consulta nativa para buscar partidos por fecha
    @Query(value = "SELECT * FROM partidoes WHERE DATE(fecha_hora) = :fecha", nativeQuery = true)
    List<Partido> findPartidosByFecha(@Param("fecha") LocalDate fecha);

    Partido findFirstByEstadio(Estadio estadio);
    @Query(value = "SELECT * FROM partidoes WHERE id = ?1", nativeQuery = true)
    Partido findByPartidoId(Long idPartido);

    @Query(value = "SELECT * FROM partidoes WHERE jornada_id = ?1 ORDER BY id", nativeQuery = true)
    List<Partido> findByJornadaId(Long idPartido);
}