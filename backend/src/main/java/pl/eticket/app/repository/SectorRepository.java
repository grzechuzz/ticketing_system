package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.Sector;
import java.util.List;
import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    List<Sector> findByVenueId(Long venueId);

    boolean existsByVenueIdAndName(Long venueId, String name);
}