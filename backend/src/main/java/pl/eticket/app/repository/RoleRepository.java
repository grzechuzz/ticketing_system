package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
