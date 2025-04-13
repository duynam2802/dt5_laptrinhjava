package ut.edu.childgrowth.services;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;
//import ut.edu.childgrowth.;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.repositories.ChildRepository;
import ut.edu.childgrowth.repositories.GrowthRecordRepository;
import ut.edu.childgrowth.repositories.UserRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
@Service
public class ChildService {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private GrowthRecordRepository growthRecordRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    // Phương thức đăng ký trẻ mới
    public Child registerChild(String authHeader, Child child) {
        String token = authHeader.substring(7);  // Cắt "Bearer " khỏi token
        String username = jwtUtil.extractUsername(token);  // Lấy username từ token
        // Tìm User entity từ DB
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Người dùng không tồn tại.");
        }
        User user = optionalUser.get();
        // Kiểm tra nếu trẻ đã tồn tại với user_id và full_name
        Optional<Child> existingChild = childRepository.findByUserAndFullName(user, child.getFullName());
        if (existingChild.isPresent()) {
            throw new IllegalArgumentException("Trẻ đã tồn tại.");
        }
        // Gán user vào child
        child.setUser(user);
        // Lưu Child vào DB và trả về
        return childRepository.save(child);
    }
    // Lấy thông tin trẻ
    public Map<String, Object> getChildrenResponse(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Thiếu token hoặc token không hợp lệ.");
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng.");
        }
        List<Child> children = getChildrenByUser(user);
        List<Map<String, Object>> danhSachCon = children.stream().map(child -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", child.getChild_id());
            data.put("hoTen", child.getFullName());
            data.put("tuoi", Period.between(child.getBirthday(), LocalDate.now()).getYears());
            data.put("BietDanh", child.getNickname());
            Map<String, Object> chiTiet = new HashMap<>();
            chiTiet.put("child_id", child.getChild_id());
            chiTiet.put("fullName", child.getFullName());
            chiTiet.put("birthday", child.getBirthday());
            chiTiet.put("gender", child.getGender());
            data.put("chiTiet", chiTiet);
            return data;
        }).toList();
        Map<String, Object> response = new HashMap<>();
        response.put("soLuongCon", children.size());
        response.put("danhSachCon", danhSachCon);
        return response;
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
    // Cập nhật chỉ số --> GrowthService



    public Child getChildByFullName(String fullName) {
        Optional <Child> child = childRepository.findByFullNameContaining(fullName);
        if (child.isEmpty()) {
            throw new RuntimeException("Trẻ với Họ tên " + fullName + " không tồn tại.");
        }
        return child.get();
    }

    // Phương thức cập nhật thông tin trẻ
    public Child updateChildInfo(Long childId, String hoTen, LocalDate ngaySinhMoi, String bietDanh, String tienSuBenh) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy trẻ có ID: " + childId));

        // Nếu có dữ liệu tăng trưởng và có đề nghị đổi ngày sinh → kiểm tra
        if (ngaySinhMoi != null) {
            List<GrowthRecord> records = growthRecordRepository.findByChildId(childId);
            for (GrowthRecord record : records) {
                if (ngaySinhMoi.isAfter(record.getThoiDiem())) {
                    throw new IllegalArgumentException("Không thể cập nhật: ngày sinh mới sau thời điểm đo tăng trưởng (" + record.getThoiDiem() + ")");
                }
            }
            child.setBirthday(ngaySinhMoi); // Hợp lệ thì set
        }

        if (hoTen != null && !hoTen.isBlank()) {
            child.setFullName(hoTen);
        }

        if (bietDanh != null) {
            child.setNickname(bietDanh);
        }

        if (tienSuBenh != null) {
            child.setMedicalHistory(tienSuBenh);
        }

        return childRepository.save(child);
    }



    public List<Child> getChildrenByUser(User user) {
        return childRepository.findByUser(user);
    }
    public Child findById(Long childId) {
        return childRepository.findById(childId).orElse(null);
    }
}