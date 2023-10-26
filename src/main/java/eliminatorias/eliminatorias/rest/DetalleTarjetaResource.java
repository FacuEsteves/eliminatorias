package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.DetalleTarjetaDTO;
import eliminatorias.eliminatorias.service.DetalleTarjetaService;
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
@RequestMapping(value = "/api/detalleTarjetas", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetalleTarjetaResource {

    private final DetalleTarjetaService detalleTarjetaService;

    public DetalleTarjetaResource(final DetalleTarjetaService detalleTarjetaService) {
        this.detalleTarjetaService = detalleTarjetaService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleTarjetaDTO>> getAllDetalleTarjetas() {
        return ResponseEntity.ok(detalleTarjetaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleTarjetaDTO> getDetalleTarjeta(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(detalleTarjetaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDetalleTarjeta(
            @RequestBody @Valid final DetalleTarjetaDTO detalleTarjetaDTO) {
        final Long createdId = detalleTarjetaService.create(detalleTarjetaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDetalleTarjeta(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DetalleTarjetaDTO detalleTarjetaDTO) {
        detalleTarjetaService.update(id, detalleTarjetaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDetalleTarjeta(@PathVariable(name = "id") final Long id) {
        detalleTarjetaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
