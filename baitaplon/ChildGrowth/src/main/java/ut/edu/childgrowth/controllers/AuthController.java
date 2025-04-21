package ut.edu.childgrowth.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ut.edu.childgrowth.dtos.AuthRequest;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }
    RedirectAttributes redirectAttributes;


    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {  // Thêm RedirectAttributes

        AuthRequest authRequest = new AuthRequest(username, password);
        try {
            AuthResponse authResponse = userService.loginUser(authRequest);
            session.setAttribute("token", authResponse.getToken());

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
            return "redirect:/index";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "login";
        }
    }



    @GetMapping("/index")
    public String showIndex(HttpSession session, Model model) {
        // ✅ Nếu muốn hiển thị token ra giao diện
        String token = (String) session.getAttribute("token");
        model.addAttribute("token", token);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa token & session khi logout
        return "redirect:/login?msg=đã đăng xuất tài khoản!";
    }

//    @GetMapping("/users/child/children-list")
//    public String showChildrenList() {
//        return "children-list"; // Spring sẽ tìm trong src/main/resources/templates
//    }

    @GetMapping("/add-child")
    public String showAddChild() {
        return "add-child"; // Spring sẽ tìm trong src/main/resources/templates
    }

//    @GetMapping("/growth-charts")
//    public String showGrowthCharts() {
//        return "growth-charts"; // Spring sẽ tìm trong src/main/resources/templates
//    }

    @GetMapping("/health-alerts")
    public String showHeathAlerts() {
        return "health-alerts"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/consultation-form")
    public String showNewConsult() {
        return "consultation"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/consultation-success")
    public String consultationSuccess() {
        return "consultation-success"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/support-request")
    public String supportRequest() {
        return "support-request"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/blog")
    public String showBlogPage() {
        return "blog"; // Trả về blog.html trong thư mục templates
    }

}
