package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "addresses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "postal_code", nullable = false, length = 12)
    private String postalCode;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(name = "building_number", nullable = false, length = 10)
    private String buildingNumber;

    @Column(name = "apartment_number", length = 10)
    private String apartmentNumber;

    @Builder.Default
    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    @Column(name="updated_at", nullable=false)
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(" ").append(buildingNumber);
        if (apartmentNumber != null && !apartmentNumber.isBlank()) {
            sb.append("/").append(apartmentNumber);
        }
        sb.append(", ").append(postalCode).append(" ").append(city);
        return sb.toString();
    }
}
