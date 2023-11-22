package eliminatorias.eliminatorias.controller;
import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.EstadioDTO;
import eliminatorias.eliminatorias.model.EstadioRequest;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.repos.CiudadRepository;
import eliminatorias.eliminatorias.repos.DetallePartidoRepository;
import eliminatorias.eliminatorias.repos.EstadioRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.UUID;
@PermitAll
@Controller
public class PartidoControllerGraphql {

 @Autowired
 private PartidoRepository partidoRepository;

 @Autowired
 private EstadioRepository estadioRepository;

    @Autowired
    private CiudadRepository ciudadRepository;
 @QueryMapping
 public List<Partido> listarPartidos() {
  return partidoRepository.findAll();
 }


    @QueryMapping
    public List<Ciudad> listarCiudad() {
        return ciudadRepository.findAll();
    }

    @QueryMapping
    public List<Estadio> listarEstadios() {
        return estadioRepository.findAll();
    }

@QueryMapping
public Partido ListarPartidoporJornada(@Argument Long id){
  return partidoRepository.findById(id).orElseThrow(
          () -> new RuntimeException(String.format("Jornada no encontrada", id))
  );
}
    @QueryMapping
    public Estadio ListarEstadios(@Argument Long id){
        return estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("Estadio no encontrado", id))
        );
    }

    @QueryMapping
    public Ciudad ListarCiudad(@Argument Long id){
        return ciudadRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("Ciudad no encontrada!", id))
        );
    }
    //GUARDAR DATOS
    @MutationMapping
    public Estadio guardarEstadio (@Argument EstadioRequest estadioRequest){
   Ciudad ciudad = ciudadRepository.findById(estadioRequest.ciudad_id()).orElse(null);
  Estadio estadioBD = new Estadio();
 //   estadioBD.setId(estadioRequest.id());
    estadioBD.setNombre(estadioRequest.nombre());
    estadioBD.setCapacidad(estadioRequest.capacidad());
  estadioBD.setCiudad(ciudad);
    return estadioRepository.save(estadioBD);
}

//ACTUALIZAR DATOS
@MutationMapping
public Estadio actualizarEstadio (@Argument Long Id, @Argument EstadioRequest estadioRequest){
    Ciudad ciudad = ciudadRepository.findById(estadioRequest.ciudad_id()).orElse(null);
    Estadio estadioBD = new Estadio();
    estadioBD.setId(Id);
    estadioBD.setNombre(estadioRequest.nombre());
    estadioBD.setCapacidad(estadioRequest.capacidad());
    estadioBD.setCiudad(ciudad);
    return estadioRepository.save(estadioBD);
}

//ELIMINAR DATOS
    @MutationMapping
public void eliminarEstadio(@Argument Long Id ){
estadioRepository.deleteById(Id);
}

}