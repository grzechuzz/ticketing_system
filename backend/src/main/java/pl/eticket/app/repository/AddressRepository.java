package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}