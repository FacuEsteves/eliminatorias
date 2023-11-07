package eliminatorias.eliminatorias.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Partidoes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Partido {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaLocal;

    @Column
    private LocalTime horaGMT;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seleccion_local_id", nullable = false)
    private Seleccion seleccionLocal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seleccion_visitante_id", nullable = false)
    private Seleccion seleccionVisitante;

    @OneToOne(mappedBy = "partido", fetch = FetchType.LAZY)
    private DetallePartido detallePartido;

    @OneToMany(mappedBy = "partido")
    private Set<DetalleTarjeta> detalleTarjetas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @OneToMany(mappedBy = "partido")
    private Set<DetalleSustitucion> detalleSustituciones;

    @OneToMany(mappedBy = "partido")
    private Set<DetalleArbitro> detalleArbitros;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
