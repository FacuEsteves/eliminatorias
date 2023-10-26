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
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "DetalleSustitucions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class DetalleSustitucion {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_partido_id", nullable = false)
    private DetallePartido detallePartido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_ingreso_id", nullable = false)
    private Jugador jugadorIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_egreso_id", nullable = false)
    private Jugador jugadorEgreso;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
