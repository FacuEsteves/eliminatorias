package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.CiudadDTO;
import eliminatorias.eliminatorias.service.CiudadService;
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
@RequestMapping(value = "/api/ciudads", produces = MediaType.APPLICATION_JSON_VALUE)
public class CiudadResource {

    private final CiudadService ciudadService;

    public CiudadResource(final CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public ResponseEntity<List<CiudadDTO>> getAllCiudads() {
        return ResponseEntity.ok(ciudadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadDTO> getCiudad(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ciudadService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCiudad(@RequestBody @Valid final CiudadDTO ciudadDTO) {
        final Long createdId = ciudadService.create(ciudadDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCiudad(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CiudadDTO ciudadDTO) {
        ciudadService.update(id, ciudadDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCiudad(@PathVariable(name = "id") final Long id) {
        ciudadService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
