package ut.edu.childgrowth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.services.ChildService;
import java.util.Optional;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    // Thêm một trẻ mới
    @PostMapping("/register")
    public Child registerChild(@RequestBody Child child) {
        return childService.registerChild(child);
    }

    // Lấy thông tin trẻ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        Optional<Child> child = childService.getChildById(id);
        return child.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy thông tin trẻ theo tên
    @GetMapping("/name/{fullName}")
    public ResponseEntity<Child> getChildByFullName(@PathVariable String fullName) {
        Optional<Child> child = childService.getChildByFullName(fullName);
        return child.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin trẻ
    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child child) {
        child.setId(id);
        Child updatedChild = childService.updateChild(id, child);
        if (updatedChild != null) {
            return ResponseEntity.ok(updatedChild);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Cập nhật chỉ số tăng trưởng (cân nặng, chiều cao, BMI)
    @PutMapping("/{id}/growth")
    public ResponseEntity<?> updateGrowthMetrics(
            @PathVariable Long id,
            @RequestBody ChildGrowthRequest growthRequest) {
        boolean success = childService.updateGrowthMetrics(id, growthRequest.getCanNang(),
                growthRequest.getChieuCao(), growthRequest.getBmi(), growthRequest.getThoiDiem());
        if (success) {
            return ResponseEntity.ok("Growth metrics updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update growth metrics.");
        }
    }
}
