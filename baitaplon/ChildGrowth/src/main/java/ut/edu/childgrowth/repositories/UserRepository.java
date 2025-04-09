package ut.edu.childgrowth.repositories;

import ut.edu.childgrowth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Tìm kiếm theo username

    Optional<User> findByEmail(String email);  // Tìm kiếm theo email

    Optional<User> findById(Long id);


    boolean existsByUsername(String username);  // Kiểm tra username đã tồn tại

    boolean existsByEmail(String email);  // Kiểm tra email đã tồn tại
}

