package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;

import java.util.Optional;

public interface GrowthRecordRepository extends JpaRepository<GrowthRecord, Long> {

    // Tìm bản ghi phát triển mới nhất của một trẻ theo thoiDiem
    Optional<GrowthRecord> findTopByChildOrderByThoiDiemDesc(Child child);
}
