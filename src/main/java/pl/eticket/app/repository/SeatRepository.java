package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Seat;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectorId(Long sectorId);

    @Modifying
    @Query("DELETE FROM Seat s WHERE s.sector.id = :sectorId")
    void deleteBySectorId(Long sectorId);
}