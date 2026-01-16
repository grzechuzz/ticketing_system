package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "ticket_holders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public static TicketHolder create(Ticket ticket, String firstName, String lastName) {
        TicketHolder holder = new TicketHolder();
        holder.ticket = ticket;
        holder.firstName = firstName;
        holder.lastName = lastName;
        return holder;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}