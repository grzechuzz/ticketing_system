package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Order;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status IN ('PENDING', 'AWAITING_PAYMENT')")
    Optional<Order> findActiveCartByUser(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = 'COMPLETED' ORDER BY o.completedAt DESC")
    List<Order> findCompletedByUserId(Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdForUpdateWithItems(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(Long id);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'EXPIRED', o.updatedAt = CURRENT_TIMESTAMP WHERE o.status IN ('PENDING', 'AWAITING_PAYMENT') AND o.reservedUntil < :now")
    int expireOrders(Instant now);
}
