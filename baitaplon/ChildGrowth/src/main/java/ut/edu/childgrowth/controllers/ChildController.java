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
import ut.edu.childgrowth.models.Alert;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.services.AlertService;
import ut.edu.childgrowth.services.ChildService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users/child")
public class ChildController {

    @Autowired
    private ChildService childService;
    @Autowired
    private AlertService alertService;

    @GetMapping("/children-list")
    public String showChildrenListPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            model.addAttribute("error", "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
            return "redirect:/login";
        }
        return "children-list";
    }

    // API trả về JSON
    @GetMapping("/api/children-list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getChildrenData(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Vui lòng đăng nhập lại");
            response.put("redirect", "/login?message=Vui+lòng+đăng+nhập+lại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            Map<String, Object> response = childService.getChildrenResponse("Bearer " + token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
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
                model.addAttribute("error", "Lỗi: Bạn cần đăng nhập để thêm trẻ.");
                return "add-child";
            }
            Child savedChild = childService.registerChild("Bearer " + token, child);
            savedChild.setUser(null);
            model.addAttribute("success", "Trẻ đã được đăng ký thành công");
            model.addAttribute("child", savedChild);
            return "children-list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "add-child";
        }
    }

    // Xử lý xóa trẻ từ form HTML
    @GetMapping("/delete/{childId}")
    public String showDeleteForm(@PathVariable Long childId, Model model) {
        model.addAttribute("childId", childId);
        return "children-list";
    }

    @PostMapping("/delete/{childId}")
    public String deleteChild(@PathVariable Long childId,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        try {
            String token = (String) session.getAttribute("token");
//            System.out.println("Token trong session: " + token);
//            System.out.println("ChildId: " + childId);
//            System.out.println("Password nhập vào: " + password);

            if (token == null) {
                throw new SecurityException("Bạn chưa đăng nhập.");
            }

            childService.deleteChild("Bearer " + token, childId, password);
            return "redirect:/users/child/children-list?success=delete_success";

        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi logic: " + e.getMessage());
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            model.addAttribute("error", "Lỗi bảo mật: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi khác: " + e.getMessage());
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

        model.addAttribute("childId", childId);
        return "children-list";
    }

    // Endpoint cải tiến cho health-alerts
    @GetMapping("/health-alerts")
    public String showHealthAlerts(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            model.addAttribute("error", "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
            return "redirect:/login";
        }

        try {
            // Lấy danh sách trẻ của người dùng
            Map<String, Object> childrenResponse = childService.getChildrenResponse("Bearer " + token);
            List<Map<String, Object>> danhSachCon = (List<Map<String, Object>>) childrenResponse.get("danhSachCon");
            List<Child> children = danhSachCon.stream()
                    .map(con -> childService.getChildById((Long) con.get("id")))
                    .toList();

            // Lấy cảnh báo chưa xử lý cho các trẻ của người dùng
            List<Alert> alerts = children.stream()
                    .flatMap(child -> alertService.getUnresolvedAlertsForChild(child).stream())
                    .toList();
            model.addAttribute("alerts", alerts);
            model.addAttribute("children", children); // Thêm danh sách trẻ cho form kiểm tra BMI
            return "health-alerts";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách cảnh báo: " + e.getMessage());
            return "health-alerts";
        }
    }

    // API REST để lấy danh sách cảnh báo chưa xử lý
    @GetMapping("/api/unresolved-alerts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUnresolvedAlerts(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Vui lòng đăng nhập lại");
            response.put("redirect", "/login?message=Vui+lòng+đăng+nhập+lại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Map<String, Object> childrenResponse = childService.getChildrenResponse("Bearer " + token);
            List<Map<String, Object>> danhSachCon = (List<Map<String, Object>>) childrenResponse.get("danhSachCon");
            List<Child> children = danhSachCon.stream()
                    .map(con -> childService.getChildById((Long) con.get("id")))
                    .toList();

            List<Alert> alerts = children.stream()
                    .flatMap(child -> alertService.getUnresolvedAlertsForChild(child).stream())
                    .toList();
            Map<String, Object> response = new HashMap<>();
            response.put("alerts", alerts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // API REST để kiểm tra BMI và tạo cảnh báo
    @PostMapping("/api/check-bmi/{childId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkBmiAlert(
            @PathVariable Long childId,
            @RequestParam double heightCm,
            @RequestParam double weightKg,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Vui lòng đăng nhập lại");
            response.put("redirect", "/login?message=Vui+lòng+đăng+nhập+lại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Child child = childService.getChildById(childId);
            if (child == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy trẻ"));
            }
            // Kiểm tra giá trị đầu vào
            if (heightCm <= 0 || weightKg <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Chiều cao và cân nặng phải lớn hơn 0"));
            }
            alertService.checkAndHandleAlert(child, heightCm, weightKg);
            return ResponseEntity.ok(Map.of("message", "Kiểm tra BMI thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // API REST để đánh dấu cảnh báo đã xử lý
    @PostMapping("/api/resolve-alert/{alertId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resolveAlert(
            @PathVariable Long alertId,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Vui lòng đăng nhập lại"));
        }

        try {
            alertService.resolveAlert(alertId);
            return ResponseEntity.ok(Map.of("message", "Đánh dấu cảnh báo đã xử lý thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }




}

