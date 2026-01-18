package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_start")
    private Instant eventStart;

    @Column(name = "event_end")
    private Instant eventEnd;

    @Column(name = "sales_start")
    private Instant salesStart;

    @Column(name = "sales_end")
    private Instant salesEnd;

    @Column(name = "max_tickets_per_customer")
    private Integer maxTicketsPerCustomer;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "holder_data_required", nullable = false)
    private Boolean holderDataRequired = false;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false, length = 20)
    private EventStatus status = EventStatus.DRAFT;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EventSector> eventSectors = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public static Event createDraft(String name, Venue venue, Organizer organizer) {
        Event event = new Event();
        event.name = name;
        event.venue = venue;
        event.organizer = organizer;
        event.status = EventStatus.DRAFT;
        return event;
    }

    public boolean isSalesOpen(Instant now) {
        if (status != EventStatus.APPROVED) {
            return false;
        }
        if (salesStart == null || salesEnd == null) {
            return false;
        }
        return now.isAfter(salesStart) && now.isBefore(salesEnd);
    }

    public void addEventSector(EventSector es) {
        eventSectors.add(es);
        es.setEvent(this);
    }
}
