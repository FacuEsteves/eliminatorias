package eliminatorias.eliminatorias.repos;

import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.Seleccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartidoRepository extends JpaRepository<Partido, Long> {

    Partido findFirstBySeleccionLocal(Seleccion seleccion);

    Partido findFirstBySeleccionVisitante(Seleccion seleccion);

    Partido findFirstByJornada(Jornada jornada);

    Partido findFirstByEstadio(Estadio estadio);

}
