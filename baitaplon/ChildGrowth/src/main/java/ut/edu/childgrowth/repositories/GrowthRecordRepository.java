package ut.edu.childgrowth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GrowthRecordRepository extends JpaRepository<GrowthRecord, Long> {

    // Tìm bản ghi phát triển mới nhất của một trẻ theo thoiDiem
    Optional<GrowthRecord> findTopByChildOrderByThoiDiemDesc(Child child);

    @Query("SELECT gr FROM GrowthRecord gr WHERE gr.child.child_id = :childId")
    List<GrowthRecord> findByChildId(@Param("childId") Long childId);

    Optional<GrowthRecord> findByChildAndThoiDiem(Child child, LocalDate thoiDiem);

    void deleteByChild(Child child);

}