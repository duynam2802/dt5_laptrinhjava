package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.repositories.ChildRepository;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    // Đăng ký trẻ mới
    public Child registerChild(Child child) {
        if (childRepository.existsByFullName(child.getFullName())) {
            throw new RuntimeException("Tên trẻ đã tồn tại!");
        }
        return childRepository.save(child);
    }

    // Tìm trẻ theo ID
    public Optional<Child> getChildById(Long id) {
        return childRepository.findById(id);
    }

    // Tìm trẻ theo tên
    public Optional<Child> getChildByFullName(String fullName) {
        return childRepository.findByFullName(fullName);
    }

    // Cập nhật thông tin trẻ
    public Child updateChild(Long id, Child updatedChild) {
        Optional<Child> existingChild = childRepository.findById(id);
        if (existingChild.isPresent()) {
            Child child = existingChild.get();
            child.setFullName(updatedChild.getFullName());
            child.setBirthday(updatedChild.getBirthday());
            child.setGender(updatedChild.getGender());
            child.setThoiDiem(updatedChild.getThoiDiem());
            child.setCanNang(updatedChild.getCanNang());
            child.setChieuCao(updatedChild.getChieuCao());
            child.setBmi(updatedChild.getBmi());
            return childRepository.save(child);
        } else {
            throw new RuntimeException("Trẻ không tồn tại!");
        }
    }

    // Cập nhật chỉ số tăng trưởng
    public boolean updateGrowthMetrics(Long id, double canNang, double chieuCao, double bmi, LocalDate thoiDiem) {
        Optional<Child> childOptional = childRepository.findById(id);
        if (childOptional.isPresent()) {
            Child child = childOptional.get();
            child.setCanNang(canNang);
            child.setChieuCao(chieuCao);
            child.setBmi(bmi);
            child.setThoiDiem(thoiDiem);
            childRepository.save(child);
            return true;
        }
        return false; // Trả về false nếu trẻ không tồn tại
    }

    // Xóa trẻ
    public void deleteChild(Long id) {
        if (childRepository.existsById(id)) {
            childRepository.deleteById(id);
        } else {
            throw new RuntimeException("Trẻ không tồn tại!");
        }
    }
}