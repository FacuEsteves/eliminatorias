package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.DetallePartidoDTO;
import eliminatorias.eliminatorias.service.DetallePartidoService;
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
@RequestMapping(value = "/api/detallePartidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetallePartidoResource {

    private final DetallePartidoService detallePartidoService;

    public DetallePartidoResource(final DetallePartidoService detallePartidoService) {
        this.detallePartidoService = detallePartidoService;
    }

    @GetMapping
    public ResponseEntity<List<DetallePartidoDTO>> getAllDetallePartidos() {
        return ResponseEntity.ok(detallePartidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePartidoDTO> getDetallePartido(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(detallePartidoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDetallePartido(
            @RequestBody @Valid final DetallePartidoDTO detallePartidoDTO) {
        final Long createdId = detallePartidoService.create(detallePartidoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDetallePartido(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DetallePartidoDTO detallePartidoDTO) {
        detallePartidoService.update(id, detallePartidoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDetallePartido(@PathVariable(name = "id") final Long id) {
        detallePartidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
