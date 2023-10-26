package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.JugadorDTO;
import eliminatorias.eliminatorias.service.JugadorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/jugadors", produces = MediaType.APPLICATION_JSON_VALUE)
public class JugadorResource {

    private final JugadorService jugadorService;

    public JugadorResource(final JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ResponseEntity<List<JugadorDTO>> getAllJugadors() {
        return ResponseEntity.ok(jugadorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorDTO> getJugador(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(jugadorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createJugador(@RequestBody @Valid final JugadorDTO jugadorDTO) {
        final Long createdId = jugadorService.create(jugadorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateJugador(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final JugadorDTO jugadorDTO) {
        jugadorService.update(id, jugadorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteJugador(@PathVariable(name = "id") final Long id) {
        jugadorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
