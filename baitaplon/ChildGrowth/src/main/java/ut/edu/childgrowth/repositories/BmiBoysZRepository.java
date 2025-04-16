package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.edu.childgrowth.models.BmiBoysZWho2007Exp;
import ut.edu.childgrowth.models.BmiGirlsZWho2007Exp;

import java.util.Optional;

@Repository
public interface BmiBoysZRepository extends JpaRepository<BmiBoysZWho2007Exp, Long> {
    Optional<BmiBoysZWho2007Exp> findByMonth(int month);
}
