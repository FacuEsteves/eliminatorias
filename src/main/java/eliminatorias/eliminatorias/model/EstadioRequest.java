package eliminatorias.eliminatorias.model;


import eliminatorias.eliminatorias.domain.Ciudad;

public record EstadioRequest(
        Long id,
         String nombre,
         Long capacidad,

        Long ciudad_id

) {

}


