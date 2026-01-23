package pl.eticket.app.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Seat;

import java.util.List;
import java.util.Set;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySectorIdOrderByRowNumberAscSeatNumberAsc(Long sectorId);

    @Query("SELECT s FROM Seat s WHERE s.sector.id = :sectorId")
    List<Seat> findAllBySectorId(Long sectorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findAllByIdForUpdate(Set<Long> seatIds);
}
