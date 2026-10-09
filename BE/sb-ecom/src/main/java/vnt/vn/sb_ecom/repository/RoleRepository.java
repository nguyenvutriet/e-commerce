package vnt.vn.sb_ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnt.vn.sb_ecom.model.AppRole;
import vnt.vn.sb_ecom.model.Roles;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(AppRole appRole);
}
