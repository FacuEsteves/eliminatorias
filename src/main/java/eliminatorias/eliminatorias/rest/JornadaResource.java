package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.JornadaDTO;
import eliminatorias.eliminatorias.service.JornadaService;
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
@RequestMapping(value = "/api/jornadas", produces = MediaType.APPLICATION_JSON_VALUE)
public class JornadaResource {

    private final JornadaService jornadaService;

    public JornadaResource(final JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public ResponseEntity<List<JornadaDTO>> getAllJornadas() {
        return ResponseEntity.ok(jornadaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JornadaDTO> getJornada(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(jornadaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createJornada(@RequestBody @Valid final JornadaDTO jornadaDTO) {
        final Long createdId = jornadaService.create(jornadaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateJornada(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final JornadaDTO jornadaDTO) {
        jornadaService.update(id, jornadaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteJornada(@PathVariable(name = "id") final Long id) {
        jornadaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
