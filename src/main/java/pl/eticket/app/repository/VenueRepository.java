package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByName(String name);
}