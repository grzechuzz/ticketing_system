package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_sectors", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "sector_id"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "capacity_snapshot")
    private Integer capacitySnapshot;

    @Column(name = "available_capacity")
    private Integer availableCapacity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "eventSector", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EventSectorTicketType> ticketTypes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void addTicketType(EventSectorTicketType tt) {
        ticketTypes.add(tt);
        tt.setEventSector(this);
    }

    public boolean isStanding() {
        return sector.getIsStanding();
    }

    public boolean hasAvailableTickets(int qty) {
        if (!isStanding()) {
            return true;
        }
        return availableCapacity != null && availableCapacity >= qty;
    }

    public boolean decrementCapacity(int qty) {
        if (!isStanding() || availableCapacity == null) {
            return true;
        }
        if (availableCapacity < qty) {
            return false;
        }
        availableCapacity -= qty;
        return true;
    }

    public void incrementCapacity(int qty) {
        if (!isStanding() || availableCapacity == null || capacitySnapshot == null) {
            return;
        }
        availableCapacity = Math.min(availableCapacity + qty, capacitySnapshot);
    }
}
