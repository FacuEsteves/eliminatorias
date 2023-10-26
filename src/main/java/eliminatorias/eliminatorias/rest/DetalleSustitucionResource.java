package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.DetalleSustitucionDTO;
import eliminatorias.eliminatorias.service.DetalleSustitucionService;
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
@RequestMapping(value = "/api/detalleSustitucions", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetalleSustitucionResource {

    private final DetalleSustitucionService detalleSustitucionService;

    public DetalleSustitucionResource(final DetalleSustitucionService detalleSustitucionService) {
        this.detalleSustitucionService = detalleSustitucionService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleSustitucionDTO>> getAllDetalleSustitucions() {
        return ResponseEntity.ok(detalleSustitucionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleSustitucionDTO> getDetalleSustitucion(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(detalleSustitucionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDetalleSustitucion(
            @RequestBody @Valid final DetalleSustitucionDTO detalleSustitucionDTO) {
        final Long createdId = detalleSustitucionService.create(detalleSustitucionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDetalleSustitucion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DetalleSustitucionDTO detalleSustitucionDTO) {
        detalleSustitucionService.update(id, detalleSustitucionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDetalleSustitucion(@PathVariable(name = "id") final Long id) {
        detalleSustitucionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
