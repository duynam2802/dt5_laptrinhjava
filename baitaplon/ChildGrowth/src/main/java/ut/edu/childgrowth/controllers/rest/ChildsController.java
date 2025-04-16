package ut.edu.childgrowth.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.services.ChildService;
import ut.edu.childgrowth.services.UserService;
import ut.edu.childgrowth.services.GrowthRecordService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/children")
public class ChildsController {

    @Autowired
    private ChildService childService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GrowthRecordService growthRecordService;

    // Đăng ký trẻ

    @PostMapping("/register")
    public ResponseEntity<?> registerChild(
            @Valid @RequestBody Child child,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Gọi service để đăng ký child
            Child savedChild = childService.registerChild(authHeader, child);

            // Xóa thông tin user khỏi savedChild
            savedChild.setUser(null); // Đảm bảo không trả về thông tin user

            // Trả về thông báo thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Trẻ đã được đăng ký thành công");
            response.put("child", savedChild);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }



    // Lấy danh sách trẻ
    @GetMapping("/show-child")
    public ResponseEntity<?> getChildrenOfUser(@RequestHeader("Authorization") String authHeader) {
        try {
            Map<String, Object> response = childService.getChildrenResponse(authHeader);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }


    // Cập nhật chỉ số
    @PutMapping("/update-growth/{childId}")
    public ResponseEntity<?> updateGrowthRecord(
            @PathVariable Long childId,
            @RequestBody Map<String, Object> payload) {
        try {
            double weight = Double.parseDouble(payload.get("canNang").toString());
            double height = Double.parseDouble(payload.get("chieuCao").toString());
            LocalDate date = payload.containsKey("thoiDiem")
                    ? LocalDate.parse(payload.get("thoiDiem").toString())
                    : null;

            String message = growthRecordService.updateGrowth(childId, weight, height, date);

            // ✅ Trả về JSON hợp lệ
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException | NoSuchElementException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PutMapping("/update-info/{childId}")
    public ResponseEntity<?> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody Map<String, Object> payload) {
        try {
            String hoTen = (String) payload.get("hoTen");
            String bietDanh = (String) payload.get("bietDanh");
            String tienSuBenh = (String) payload.get("tienSuBenh");
            LocalDate ngaySinh = LocalDate.parse(payload.get("ngaySinh").toString());

            Child updatedChild = childService.updateChildInfo(childId, hoTen, ngaySinh, bietDanh, tienSuBenh);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cập nhật thông tin trẻ thành công");
            response.put("child", updatedChild);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy trẻ: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }






}

