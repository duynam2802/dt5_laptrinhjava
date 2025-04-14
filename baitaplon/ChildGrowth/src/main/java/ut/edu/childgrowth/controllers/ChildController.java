package ut.edu.childgrowth.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.services.ChildService;

import java.util.HashMap;
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


    @GetMapping("/add-child")
    public String showAddChildForm(Model model) {
        model.addAttribute("child", new Child());  // Khởi tạo đối tượng Child
        return "add-child";  // Trả về trang add-child.html
    }

    @PostMapping("/add-child")
    public String registerChild(
            @Valid @ModelAttribute("child") Child child,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "add-child";
        }
        try {
            String token = (String) session.getAttribute("token");
            if (token == null) {
                model.addAttribute("message", "Lỗi: Bạn cần đăng nhập để thêm trẻ.");
                return "add-child";
            }
            Child savedChild = childService.registerChild("Bearer " + token, child);
            savedChild.setUser(null);
            model.addAttribute("message", "Trẻ đã được đăng ký thành công");
            model.addAttribute("child", savedChild);
            return "add-child";
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
            return "add-child";
        }
    }



}




