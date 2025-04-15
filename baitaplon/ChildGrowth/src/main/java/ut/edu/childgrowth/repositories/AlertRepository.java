package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.childgrowth.models.Alert;
import ut.edu.childgrowth.models.Child;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    boolean existsByChildAndAlertTypeAndResolvedFalse(Child child, String alertType);

    List<Alert> findByChildAndResolvedFalse(Child child);
}
