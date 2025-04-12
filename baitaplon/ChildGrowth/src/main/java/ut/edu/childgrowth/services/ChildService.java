package ut.edu.childgrowth.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;
//import ut.edu.childgrowth.;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.repositories.ChildRepository;
import ut.edu.childgrowth.repositories.GrowthRecordRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private GrowthRecordRepository growthRecordRepository;

    // Các phương thức khác...

    public boolean updateGrowthMetrics(Long childId, double canNang, double chieuCao, double bmi, LocalDate thoiDiem) {
        // Lấy trẻ theo ID
        Child child = childRepository.findById(childId).orElse(null);

        if (child == null) {
            return false;
        }

        // Tìm bản ghi phát triển mới nhất của trẻ
        Optional<GrowthRecord> optionalRecord = growthRecordRepository.findTopByChildOrderByThoiDiemDesc(child);

        GrowthRecord growthRecord;
        if (optionalRecord.isPresent()) {
            // Nếu có bản ghi phát triển mới nhất, cập nhật thông tin
            growthRecord = optionalRecord.get();
            growthRecord.setCanNang(canNang);
            growthRecord.setChieuCao(chieuCao);
            growthRecord.setBmi(bmi);
            growthRecord.setThoiDiem(thoiDiem);
        } else {
            // Nếu không có bản ghi phát triển nào, tạo mới
            growthRecord = new GrowthRecord(child, thoiDiem, canNang, chieuCao, bmi);
        }

        // Lưu lại bản ghi phát triển
        growthRecordRepository.save(growthRecord);
        return true;
    }

    // Phương thức đăng ký trẻ mới
    public Child registerChild(@Valid Child child) {
        // Kiểm tra nếu đã tồn tại trẻ cùng tên và cùng user
        boolean exists = childRepository.existsByFullNameAndUser(child.getFullName(), child.getUser());
        if (exists) {
            throw new IllegalArgumentException("Trẻ đã tồn tại với tên này cho người dùng này.");
        }

        return childRepository.save(child);
    }



    // Phương thức lấy thông tin trẻ theo ID
    public Child getChildById(Long id) {
        // Tìm trẻ trong cơ sở dữ liệu theo ID
        Optional<Child> child = childRepository.findById(id);

        // Kiểm tra nếu không tìm thấy trẻ theo ID
        if (child.isEmpty()) {
            throw new RuntimeException("Trẻ với ID " + id + " không tồn tại.");
        }

        // Trả về đối tượng Child nếu tìm thấy
        return child.get();
    }

    public Child getChildByFullName(String fullName) {
        Optional <Child> child = childRepository.findByFullNameContaining(fullName);
        if (child.isEmpty()) {
            throw new RuntimeException("Trẻ với Họ tên " + fullName + " không tồn tại.");
        }
        return child.get();
    }

    // Phương thức cập nhật thông tin trẻ
    public Child updateChild(Child updatedChild) {
        // Kiểm tra xem trẻ với ID có tồn tại hay không
        Optional<Child> existingChildOpt = childRepository.findById(updatedChild.getChild_id());

        if (existingChildOpt.isEmpty()) {
            throw new RuntimeException("Trẻ với ID " + updatedChild.getChild_id() + " không tồn tại.");
        }

        // Lấy đối tượng trẻ hiện tại từ cơ sở dữ liệu
        Child existingChild = existingChildOpt.get();

        // Cập nhật thông tin của trẻ
        existingChild.setFullName(updatedChild.getFullName());
        existingChild.setBirthday(updatedChild.getBirthday());
        existingChild.setGender(updatedChild.getGender());
        //existingChild.setUser(updatedChild.getUser());

        // Lưu lại đối tượng trẻ đã được cập nhật vào cơ sở dữ liệu
        return childRepository.save(existingChild);
    }

    public List<Child> getChildrenByUser(User user) {
        return childRepository.findByUser(user);
    }

    public Child findById(Long childId) {
        return childRepository.findById(childId).orElse(null);
    }
}
