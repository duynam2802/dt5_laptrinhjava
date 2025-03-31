package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.edu.childgrowth.models.Child;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    Optional<Child> findByFullName(String fullName);

    boolean existsByFullName(String fullName);
}