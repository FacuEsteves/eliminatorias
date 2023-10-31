package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.DetalleArbitroDTO;
import eliminatorias.eliminatorias.service.DetalleArbitroService;
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
@RequestMapping(value = "/api/detalleArbitros", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetalleArbitroResource {

    private final DetalleArbitroService detalleArbitroService;

    public DetalleArbitroResource(final DetalleArbitroService detalleArbitroService) {
        this.detalleArbitroService = detalleArbitroService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleArbitroDTO>> getAllDetalleArbitros() {
        return ResponseEntity.ok(detalleArbitroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleArbitroDTO> getDetalleArbitro(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(detalleArbitroService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDetalleArbitro(
            @RequestBody @Valid final DetalleArbitroDTO detalleArbitroDTO) {
        final Long createdId = detalleArbitroService.create(detalleArbitroDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDetalleArbitro(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DetalleArbitroDTO detalleArbitroDTO) {
        detalleArbitroService.update(id, detalleArbitroDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDetalleArbitro(@PathVariable(name = "id") final Long id) {
        detalleArbitroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
