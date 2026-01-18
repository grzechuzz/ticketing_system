package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sectors", uniqueConstraints = @UniqueConstraint(columnNames = {"venue_id", "name"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "is_standing", nullable = false)
    private Boolean isStanding = false;

    @Column(name = "standing_capacity")
    private Integer standingCapacity;

    @Column(name = "rows_count")
    private Integer rowsCount;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

    @Column(name = "position_x", nullable = false)
    private Integer positionX = 0;

    @Column(name = "position_y", nullable = false)
    private Integer positionY = 0;

    @Column(nullable = false)
    private Integer width = 100;

    @Column(nullable = false)
    private Integer height = 100;

    @Column(length = 7)
    private String color = "#3B82F6";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("rowNumber ASC, seatNumber ASC")
    private List<Seat> seats = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public int getCapacity() {
        if (isStanding) {
            return standingCapacity != null ? standingCapacity : 0;
        }
        return (rowsCount != null && seatsPerRow != null) ? rowsCount * seatsPerRow : seats.size();
    }
}
