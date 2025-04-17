package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;
import ut.edu.childgrowth.repositories.GrowthRecordRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GrowthRecordService {

    @Autowired
    private GrowthRecordRepository growthRecordRepository;

    @Autowired
    ChildService childService;
    @Autowired
    ChildService childRepository;



    public GrowthRecord saveGrowthRecord(GrowthRecord growthRecord) {
        return growthRecordRepository.save(growthRecord);
    }

    public void save(GrowthRecord newRecord) {
        growthRecordRepository.save(newRecord);
    }

    public String updateGrowth(Long childId, double weight, double height, LocalDate date) {
        if (weight <= 0.5 || weight > 300) {
            throw new IllegalArgumentException("Cân nặng không hợp lệ.");
        }
        if (height <= 10 || height > 250) {
            throw new IllegalArgumentException("Chiều cao không hợp lệ.");
        }

        Child child = childService.findById(childId);
        if (child == null) {
            throw new NoSuchElementException("Không tìm thấy trẻ.");
        }

        LocalDate thoiDiem = (date != null) ? date : LocalDate.now();

        // Kiểm tra và xóa bản ghi cũ nếu đã tồn tại trong ngày
        Optional<GrowthRecord> existingRecord = growthRecordRepository
                .findByChildAndThoiDiem(child, thoiDiem);

        existingRecord.ifPresent(record -> growthRecordRepository.delete(record));

        // Lưu bản ghi mới
        GrowthRecord newRecord = new GrowthRecord(child, thoiDiem, weight, height);
        growthRecordRepository.save(newRecord);

        return "Đã cập nhật thông tin thành công";
    }


    public Child getChildById(Long childId) {
        Optional<Child> childOptional = Optional.ofNullable(childRepository.findById(childId));
        if (childOptional.isPresent()) {
            return childOptional.get();
        } else {
            throw new RuntimeException("Không tìm thấy trẻ với ID: " + childId);
        }
    }


}
