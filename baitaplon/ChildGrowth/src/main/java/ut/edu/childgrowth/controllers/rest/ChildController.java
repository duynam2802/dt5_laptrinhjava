package ut.edu.childgrowth.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.services.ChildService;
import ut.edu.childgrowth.services.UserService;
import ut.edu.childgrowth.services.GrowthRecordService;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/children")
public class ChildController {

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
            // Cắt bỏ tiền tố "Bearer " để lấy token
            String token = authHeader.substring(7);

            // Lấy username từ token
            String username = jwtUtil.extractUsername(token);

            // Tìm user trong hệ thống và lấy thông tin từ UserService
            UserResponse userResponse = userService.getUserByUsername(username);
            if (userResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
            }

            // Chuyển thông tin từ UserResponse sang User (nếu cần thiết)
            User user = new User();
            user.setUser_id(userResponse.getUserId());
            user.setUsername(userResponse.getUsername());
            user.setEmail(userResponse.getEmail());
            user.setFullName(userResponse.getFullName());

            // Gán user cho trẻ trước khi lưu
            child.setUser(user);
            Child savedChild = childService.registerChild(child);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedChild);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    // Lấy danh sách trẻ
    @GetMapping("/show-child")
    public ResponseEntity<?> getChildrenOfUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thiếu token hoặc token không hợp lệ.");
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            // Lấy thông tin người dùng từ username
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng.");
            }

            // Lấy danh sách con theo user
            List<Child> children = childService.getChildrenByUser(user);

            // Đếm số lượng
            int soLuong = children.size();

            // Tạo DTO phản hồi
            List<Map<String, Object>> danhSachCon = children.stream().map(child -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", child.getChild_id());
                data.put("hoTen", child.getFullName());
                data.put("tuoi", Period.between(child.getBirthday(), LocalDate.now()).getYears());

                // Tạo map riêng cho chi tiết mà không bao gồm thông tin của user
                Map<String, Object> childDetails = new HashMap<>();
                childDetails.put("child_id", child.getChild_id());
                childDetails.put("fullName", child.getFullName());
                childDetails.put("birthday", child.getBirthday());
                childDetails.put("gender", child.getGender());
//                childDetails.put("height", child.getHeight());
//                childDetails.put("weight", child.getWeight());
                // Không bao gồm thông tin về User

                data.put("chiTiet", childDetails); // chỉ trả về thông tin chi tiết trẻ
                return data;
            }).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("soLuongCon", soLuong);
            response.put("danhSachCon", danhSachCon);

            return ResponseEntity.ok(response);

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
            if (!payload.containsKey("canNang") || !payload.containsKey("chieuCao")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiếu thông tin bắt buộc.");
            }

            double canNang = Double.parseDouble(payload.get("canNang").toString());
            double chieuCao = Double.parseDouble(payload.get("chieuCao").toString());

            // Kiểm tra tính hợp lệ của chiều cao và cân nặng
            if (canNang <= 0 || canNang > 300) {  // Giới hạn cân nặng (từ 0 đến 200 kg)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cân nặng không hợp lệ.");
            }

            if (chieuCao <= 10 || chieuCao > 250) {  // Giới hạn chiều cao (từ 30 cm đến 250 cm)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chiều cao không hợp lệ.");
            }

            // Nếu không có thoiDiem, gán ngày hiện tại
            LocalDate thoiDiem = payload.containsKey("thoiDiem")
                    ? LocalDate.parse(payload.get("thoiDiem").toString())
                    : LocalDate.now();  // Gán ngày hiện tại nếu không có

            Child child = childService.findById(childId);
            if (child == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy trẻ.");
            }

            GrowthRecord record = new GrowthRecord(child, thoiDiem, canNang, chieuCao);
            growthRecordService.save(record);

            return ResponseEntity.ok("Đã cập nhật thông tin thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }




}

