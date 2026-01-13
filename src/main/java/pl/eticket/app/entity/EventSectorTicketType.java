package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Entity
@Table(name = "event_sector_ticket_types", uniqueConstraints = @UniqueConstraint(columnNames = {"event_sector_id", "ticket_type_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSectorTicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_sector_id", nullable = false)
    private EventSector eventSector;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "price_net", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceNet;

    @Builder.Default
    @Column(name = "vat_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal vatRate = new BigDecimal("1.23");

    @Column(name = "price_gross", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal priceGross;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public BigDecimal calculatePriceGross() {
        if (priceNet == null || vatRate == null) return BigDecimal.ZERO;
        return priceNet.multiply(vatRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPriceGrossCalculated() {
        return priceGross != null ? priceGross : calculatePriceGross();
    }
}
