package ut.edu.childgrowth.repositories;

import ut.edu.childgrowth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Tìm kiếm người dùng theo email
}
