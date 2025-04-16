package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.edu.childgrowth.models.BmiGirlsZWho2007Exp;

import java.util.Optional;

@Repository
public interface BmiGirlsZRepository extends JpaRepository<BmiGirlsZWho2007Exp, Long> {
    Optional<BmiGirlsZWho2007Exp> findByMonth(int month);
}
