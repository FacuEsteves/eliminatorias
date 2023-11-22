package eliminatorias.eliminatorias.Procesos;


import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.User;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.service.PartidoService;
import eliminatorias.eliminatorias.service.UserServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.LocalDate;
import java.util.List;
@EnableScheduling
@Component
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class InicioSegundoPlano implements CommandLineRunner {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserServiceImpl userService;

    private final PartidoService partidoService;

    public InicioSegundoPlano(PartidoService partidoService){
        this.partidoService = partidoService;
    }

    public static void main(final String[] args) {
        SpringApplication.run(InicioSegundoPlano.class, args);
    }

    //@Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Ejecutar cada 24 horas en milisegundos
    @Scheduled(fixedRate = 10000) // ejecutar cada 5 segundos
    public void ejecutarTareaDiaria() {
        try {

          /*  List<User> users = userService.findAll();

            for (User user : users) {
                logger.info(user.getEmail());
            }
        */
            LocalDate fechaActual = LocalDate.of(2023, 11, 21);
            List<PartidoDTO> partidos = partidoService.findPartidosByFecha(fechaActual);

            for (PartidoDTO partido : partidos) {
                logger.info("-----------------------------");
                logger.info(partido.getSeleccionLocalNombre() + " vs " + partido.getSeleccionVisitanteNombre());
                logger.info(partido.getHoraLocal() + " " + partido.getEstadioNombre());
            }
        } catch (Exception e) {
            logger.error("Error al ejecutar la tarea diaria: " + e.getMessage());
            // Tratar la excepción según tu lógica de manejo de errores
        }
    }

    @Override
    public void run(String... args) throws Exception {
      //  logger.info("Mostrando correos: ");
    }
}
