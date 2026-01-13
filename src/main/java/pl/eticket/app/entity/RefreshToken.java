package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="auth_refresh_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="token_hash", nullable=false)
    private String tokenHash;

    @Column(name="expires_at", nullable=false)
    private Instant expiresAt;

    @Column(name="revoked_at")
    private Instant revokedAt;

    @Column(name="ip_address", length=45)
    private String ipAddress;

    @Column(name="user_agent", length=500)
    private String userAgent;

    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt;

    @Column(name="last_used_at")
    private Instant lastUsedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isValid() {
        return !isExpired() && !isRevoked();
    }

    public void revoke() {
        this.revokedAt = Instant.now();
    }
}
