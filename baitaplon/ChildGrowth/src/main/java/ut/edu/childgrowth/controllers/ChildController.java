package ut.edu.childgrowth.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ut.edu.childgrowth.services.ChildService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users/child")
public class ChildController {

    @Autowired
    private ChildService childService;

    @GetMapping("/children-list")
    public ResponseEntity<Map<String, Object>> getChildrenPage(HttpSession session) {
        String token = (String) session.getAttribute("token");

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Bạn cần đăng nhập để xem danh sách trẻ."));
        }

        try {
            Map<String, Object> response = childService.getChildrenResponse("Bearer " + token);
            return ResponseEntity.ok(response); // Trả về JSON hợp lệ
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }


}

