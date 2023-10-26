package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.PaisDTO;
import eliminatorias.eliminatorias.service.PaisService;
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
@RequestMapping(value = "/api/paiss", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaisResource {

    private final PaisService paisService;

    public PaisResource(final PaisService paisService) {
        this.paisService = paisService;
    }

    @GetMapping
    public ResponseEntity<List<PaisDTO>> getAllPaiss() {
        return ResponseEntity.ok(paisService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisDTO> getPais(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(paisService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPais(@RequestBody @Valid final PaisDTO paisDTO) {
        final Long createdId = paisService.create(paisDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePais(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PaisDTO paisDTO) {
        paisService.update(id, paisDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePais(@PathVariable(name = "id") final Long id) {
        paisService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
