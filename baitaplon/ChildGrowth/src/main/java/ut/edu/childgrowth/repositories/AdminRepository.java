package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.childgrowth.models.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

