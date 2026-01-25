package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Organizer;
import java.util.List;
import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByEmail(String email);
    List<Organizer> findByIsActiveTrue();

    @Query("SELECT o FROM Organizer o JOIN o.users u WHERE u.id = :userId AND o.isActive = true")
    List<Organizer> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Organizer o JOIN o.users u WHERE o.id = :orgId AND u.id = :userId")
    boolean isUserMemberOfOrganizer(Long orgId, Long userId);
}