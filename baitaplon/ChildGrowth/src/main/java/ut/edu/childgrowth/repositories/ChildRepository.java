package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.edu.childgrowth.models.Child;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    // Tìm trẻ theo ID
    Optional<Child> findById(Long id);

    // Tìm trẻ theo tên
    Optional<Child> findByFullNameContaining(String fullName);


    boolean existsByFullName(String fullName);
}
