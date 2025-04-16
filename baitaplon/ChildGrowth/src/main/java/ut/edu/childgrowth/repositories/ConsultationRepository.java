package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.childgrowth.models.Consultation;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findById(Long childId); // Truy vấn theo child_id của Child
}




