package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    private static final int DEFAULT_RESERVATION_MINUTES = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price_net", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPriceNet = BigDecimal.ZERO;

    @Column(name = "total_price_gross", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPriceGross = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.NAMED_ENUM)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "reserved_until", nullable = false)
    private Instant reservedUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(name = "completed_at")
    private Instant completedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public static Order create(User user) {
        Order order = new Order();
        order.user = user;
        order.status = OrderStatus.PENDING;
        order.totalPriceNet = BigDecimal.ZERO;
        order.totalPriceGross = BigDecimal.ZERO;
        order.reservedUntil = Instant.now().plus(DEFAULT_RESERVATION_MINUTES, ChronoUnit.MINUTES);
        return order;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (reservedUntil == null) {
            reservedUntil = Instant.now().plus(DEFAULT_RESERVATION_MINUTES, ChronoUnit.MINUTES);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(reservedUntil) &&
                (status == OrderStatus.PENDING || status == OrderStatus.AWAITING_PAYMENT);
    }

    public boolean canCheckout() {
        return status == OrderStatus.PENDING && !items.isEmpty() && !isExpired();
    }

    public void refreshReservation() {
        reservedUntil = Instant.now().plus(DEFAULT_RESERVATION_MINUTES, ChronoUnit.MINUTES);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotals();
        refreshReservation();
    }

    public void recalculateTotals() {
        totalPriceNet = items.stream()
                .map(OrderItem::getTotalPriceNet)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalPriceGross = items.stream()
                .map(OrderItem::getTotalPriceGross)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
