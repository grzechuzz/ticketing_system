package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name = :roleName AND u.isActive = true")
    List<User> findByRoleName(String roleName);
}
