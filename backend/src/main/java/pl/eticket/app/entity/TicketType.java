package pl.eticket.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_types")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketType {

    public static final String NORMAL = "NORMAL";
    public static final String REDUCED = "REDUCED";
    public static final String VIP = "VIP";
    public static final String CHILD = "CHILD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}
