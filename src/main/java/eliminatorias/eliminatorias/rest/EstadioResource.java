package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.EstadioDTO;
import eliminatorias.eliminatorias.service.EstadioService;
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
@RequestMapping(value = "/api/estadios", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadioResource {

    private final EstadioService estadioService;

    public EstadioResource(final EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @GetMapping
    public ResponseEntity<List<EstadioDTO>> getAllEstadios() {
        return ResponseEntity.ok(estadioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getEstadio(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(estadioService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEstadio(@RequestBody @Valid final EstadioDTO estadioDTO) {
        final Long createdId = estadioService.create(estadioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEstadio(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EstadioDTO estadioDTO) {
        estadioService.update(id, estadioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEstadio(@PathVariable(name = "id") final Long id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
