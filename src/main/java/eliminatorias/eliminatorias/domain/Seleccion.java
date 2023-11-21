package eliminatorias.eliminatorias.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "Seleccions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Seleccion {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column
    private String sigla;

    @Column(columnDefinition = "BLOB")
    private byte[] escudo;

    @Column
    private String escudoMimeType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pais_id", nullable = false, unique = true)
    private Pais pais;

    @OneToMany(mappedBy = "seleccion")
    private Set<Jugador> jugadores;

    @OneToMany(mappedBy = "seleccionLocal")
    private Set<Partido> partidosLocal;

    @OneToMany(mappedBy = "seleccionVisitante")
    private Set<Partido> partidosVisitante;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
