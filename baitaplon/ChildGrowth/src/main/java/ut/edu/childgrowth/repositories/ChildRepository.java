package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.User;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    boolean existsByFullName(String fullName);
    Optional<Child> findByFullNameContaining(String fullName);
    boolean existsByFullNameAndUser(String fullName, User user); // New method

    List<Child> findByUser(User user);
}
