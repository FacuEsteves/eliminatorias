package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.TipoArbitroDTO;
import eliminatorias.eliminatorias.service.TipoArbitroService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/tipoArbitros", produces = MediaType.APPLICATION_JSON_VALUE)
public class TipoArbitroResource {

    private final TipoArbitroService tipoArbitroService;

    public TipoArbitroResource(final TipoArbitroService tipoArbitroService) {
        this.tipoArbitroService = tipoArbitroService;
    }

    @GetMapping
    public ResponseEntity<List<TipoArbitroDTO>> getAllTipoArbitros() {
        return ResponseEntity.ok(tipoArbitroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoArbitroDTO> getTipoArbitro(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tipoArbitroService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTipoArbitro(
            @RequestBody @Valid final TipoArbitroDTO tipoArbitroDTO) {
        final Long createdId = tipoArbitroService.create(tipoArbitroDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTipoArbitro(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TipoArbitroDTO tipoArbitroDTO) {
        tipoArbitroService.update(id, tipoArbitroDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTipoArbitro(@PathVariable(name = "id") final Long id) {
        tipoArbitroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
