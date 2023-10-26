package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.SeleccionDTO;
import eliminatorias.eliminatorias.service.SeleccionService;
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
@RequestMapping(value = "/api/seleccions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeleccionResource {

    private final SeleccionService seleccionService;

    public SeleccionResource(final SeleccionService seleccionService) {
        this.seleccionService = seleccionService;
    }

    @GetMapping
    public ResponseEntity<List<SeleccionDTO>> getAllSeleccions() {
        return ResponseEntity.ok(seleccionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeleccionDTO> getSeleccion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(seleccionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSeleccion(
            @RequestBody @Valid final SeleccionDTO seleccionDTO) {
        final Long createdId = seleccionService.create(seleccionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSeleccion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SeleccionDTO seleccionDTO) {
        seleccionService.update(id, seleccionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSeleccion(@PathVariable(name = "id") final Long id) {
        seleccionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
