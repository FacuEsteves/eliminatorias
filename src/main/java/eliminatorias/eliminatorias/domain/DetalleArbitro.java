package eliminatorias.eliminatorias.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "DetalleArbitroes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class DetalleArbitro {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arbitro_id", nullable = false, unique = true)
    private Arbitro arbitro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_arbitro_id", nullable = false)
    private TipoArbitro tipoArbitro;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
