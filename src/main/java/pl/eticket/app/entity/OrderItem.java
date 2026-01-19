package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // denormalize on purpose to avoid overselling!!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_sector_ticket_type_id", nullable = false)
    private EventSectorTicketType eventSectorTicketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price_net", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceNet;

    @Column(name = "unit_price_gross", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceGross;

    @Column(name = "vat_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "event_name_snapshot", nullable = false)
    private String eventNameSnapshot;

    @Column(name = "event_date_snapshot", nullable = false)
    private Instant eventDateSnapshot;

    @Column(name = "venue_name_snapshot", nullable = false)
    private String venueNameSnapshot;

    @Column(name = "venue_address_snapshot", nullable = false, length = 500)
    private String venueAddressSnapshot;

    @Column(name = "sector_name_snapshot", nullable = false, length = 100)
    private String sectorNameSnapshot;

    @Column(name = "row_snapshot")
    private Integer rowSnapshot;

    @Column(name = "seat_number_snapshot")
    private Integer seatNumberSnapshot;

    @Column(name = "ticket_type_name_snapshot", nullable = false, length = 100)
    private String ticketTypeNameSnapshot;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    public static OrderItem createForSeat(Order order, EventSectorTicketType tt, Seat seat) {
        OrderItem item = createBase(order, tt);
        item.seat = seat;
        item.quantity = 1;
        item.rowSnapshot = seat.getRowNumber();
        item.seatNumberSnapshot = seat.getSeatNumber();
        return item;
    }

    public static OrderItem createForStanding(Order order, EventSectorTicketType tt, int quantity) {
        OrderItem item = createBase(order, tt);
        item.quantity = quantity;
        return item;
    }

    // Common logic for seating and GA tickets
    private static OrderItem createBase(Order order, EventSectorTicketType tt) {
        Event event = tt.getEventSector().getEvent();
        Venue venue = event.getVenue();
        Sector sector = tt.getEventSector().getSector();

        OrderItem item = new OrderItem();
        item.order = order;
        item.event = event;
        item.eventSectorTicketType = tt;
        item.unitPriceNet = tt.getPriceNet();
        item.unitPriceGross = tt.getPriceGrossCalculated();
        item.vatRate = tt.getVatRate();

        item.eventNameSnapshot = event.getName();
        item.eventDateSnapshot = event.getEventStart();
        item.venueNameSnapshot = venue.getName();
        item.venueAddressSnapshot = venue.getAddress().getFullAddress();
        item.sectorNameSnapshot = sector.getName();
        item.ticketTypeNameSnapshot = tt.getTicketType().getName();

        return item;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setOrderItem(this);
    }
}