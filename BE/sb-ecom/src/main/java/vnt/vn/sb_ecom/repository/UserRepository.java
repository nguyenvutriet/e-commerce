package vnt.vn.sb_ecom.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vnt.vn.sb_ecom.model.AppRole;
import vnt.vn.sb_ecom.model.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUserName(String username);

    boolean existsByUserName(@NotBlank @Size(min = 3, max = 20) String username);

    boolean existsByEmail(@NotBlank @Size(max = 50) String email);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.roleName = :role")
    Page<Users> findByRoleName(@Param("role") AppRole appRole, Pageable pageable);
}
