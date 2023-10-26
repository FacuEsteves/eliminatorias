package eliminatorias.eliminatorias.rest;

import eliminatorias.eliminatorias.model.ArbitroDTO;
import eliminatorias.eliminatorias.service.ArbitroService;
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
@RequestMapping(value = "/api/arbitros", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArbitroResource {

    private final ArbitroService arbitroService;

    public ArbitroResource(final ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping
    public ResponseEntity<List<ArbitroDTO>> getAllArbitros() {
        return ResponseEntity.ok(arbitroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArbitroDTO> getArbitro(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(arbitroService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createArbitro(@RequestBody @Valid final ArbitroDTO arbitroDTO) {
        final Long createdId = arbitroService.create(arbitroDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateArbitro(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ArbitroDTO arbitroDTO) {
        arbitroService.update(id, arbitroDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteArbitro(@PathVariable(name = "id") final Long id) {
        arbitroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
