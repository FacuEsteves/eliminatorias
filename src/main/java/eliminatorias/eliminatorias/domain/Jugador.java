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
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Jugadors")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Jugador {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column
    private Integer edad;

    @Column(nullable = false)
    private String posicion;

    @Column
    private Integer dorsal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seleccion_id", nullable = false)
    private Seleccion seleccion;

    @OneToMany(mappedBy = "jugador")
    private Set<DetalleTarjeta> detalleTarjetas;

    @OneToMany(mappedBy = "jugadorIngreso")
    private Set<DetalleSustitucion> detalleSustitucionesIngreso;

    @OneToMany(mappedBy = "jugadorEgreso")
    private Set<DetalleSustitucion> detalleSustitucionesEgreso;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
