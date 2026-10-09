package vnt.vn.sb_ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnt.vn.sb_ecom.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
