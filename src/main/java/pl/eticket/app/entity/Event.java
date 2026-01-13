package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "event_start", nullable = false)
    private Instant eventStart;

    @Column(name = "event_end", nullable = false)
    private Instant eventEnd;

    @Column(name = "sales_start", nullable = false)
    private Instant salesStart;

    @Column(name = "sales_end", nullable = false)
    private Instant salesEnd;

    @Column(name = "max_tickets_per_customer")
    private Integer maxTicketsPerCustomer;

    @Column(name = "min_age")
    private Integer minAge;

    @Builder.Default
    @Column(name = "holder_data_required", nullable = false)
    private Boolean holderDataRequired = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
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

    @Builder.Default
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

    public boolean isSalesOpen() {
        if (status != EventStatus.APPROVED) return false;
        Instant now = Instant.now();
        return now.isAfter(salesStart) && now.isBefore(salesEnd);
    }

    public void submitForReview() {
        if (status != EventStatus.DRAFT) throw new IllegalStateException("Only draft events can be submitted");
        this.status = EventStatus.PENDING;
    }

    public void approve() {
        if (status != EventStatus.PENDING) throw new IllegalStateException("Only pending events can be approved");
        this.status = EventStatus.APPROVED;
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        if (status != EventStatus.PENDING) throw new IllegalStateException("Only pending events can be rejected");
        this.status = EventStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public void addEventSector(EventSector es) {
        eventSectors.add(es);
        es.setEvent(this);
    }
}
