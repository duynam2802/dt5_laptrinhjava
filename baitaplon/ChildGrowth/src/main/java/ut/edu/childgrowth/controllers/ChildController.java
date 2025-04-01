package ut.edu.childgrowth.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.services.ChildService;

import java.util.Optional;

@RestController
@RequestMapping("/api/children") // Đường dẫn chung cho tất cả các yêu cầu
public class ChildController {

    @Autowired
    private ChildService childService;

    // Thêm một trẻ mới (Đăng ký trẻ)
    @PostMapping("/register")
    public ResponseEntity<Child> registerChild(@Valid @RequestBody Child child) {
        // Gọi service để đăng ký trẻ mới
        Child registeredChild = childService.registerChild(child);
        return ResponseEntity.ok(registeredChild);
    }

    // Lấy thông tin trẻ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        Optional<Child> child = Optional.ofNullable(childService.getChildById(id));
        return child.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy thông tin trẻ theo tên đầy đủ
    @GetMapping("/name/{fullName}")
    public ResponseEntity<Child> getChildByFullName(@PathVariable String fullName) {
        Optional<Child> child = Optional.ofNullable(childService.getChildByFullName(fullName));
        return child.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin của trẻ (ví dụ: fullName, birthday, gender)
    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child updatedChild) {
        Child existingChild = childService.getChildById(id);
        if (existingChild == null) {
            return ResponseEntity.notFound().build();
        }
        // Gọi service để cập nhật trẻ
        updatedChild.setId(id);
        Child updated = childService.updateChild(updatedChild);
        return ResponseEntity.ok(updated);
    }

    // Cập nhật chỉ số tăng trưởng của trẻ (ví dụ: cân nặng, chiều cao, BMI)
    @PutMapping("/{id}/growth")
    public ResponseEntity<?> updateGrowthMetrics(@PathVariable Long id, @RequestBody ChildGrowthRequest growthRequest) {
        boolean success = childService.updateGrowthMetrics(id, growthRequest.getCanNang(),
                growthRequest.getChieuCao(), growthRequest.getBmi(), growthRequest.getThoiDiem());
        if (success) {
            return ResponseEntity.ok("Growth metrics updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update growth metrics.");
        }
    }
}
