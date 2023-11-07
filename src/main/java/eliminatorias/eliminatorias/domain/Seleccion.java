package eliminatorias.eliminatorias.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


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

    @Column(columnDefinition = "longtext")
    private String escudo;

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
