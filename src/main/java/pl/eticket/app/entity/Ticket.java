package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(nullable = false, unique = true)
    private UUID code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.NAMED_ENUM)
    private TicketStatus status = TicketStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "used_at")
    private Instant usedAt;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TicketHolder holder;

    public static Ticket create(OrderItem orderItem) {
        Ticket ticket = new Ticket();
        ticket.orderItem = orderItem;
        ticket.code = UUID.randomUUID();
        ticket.status = TicketStatus.ACTIVE;
        return ticket;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        if (code == null) code = UUID.randomUUID();
    }
}