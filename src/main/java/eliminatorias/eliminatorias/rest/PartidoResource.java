package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.service.PartidoService;
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
@RequestMapping(value = "/api/partidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartidoResource {

    private final PartidoService partidoService;

    public PartidoResource(final PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public ResponseEntity<List<PartidoDTO>> getAllPartidos() {
        return ResponseEntity.ok(partidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> getPartido(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(partidoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPartido(@RequestBody @Valid final PartidoDTO partidoDTO) {
        final Long createdId = partidoService.create(partidoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePartido(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final PartidoDTO partidoDTO) {
        partidoService.update(id, partidoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePartido(@PathVariable(name = "id") final Long id) {
        partidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
