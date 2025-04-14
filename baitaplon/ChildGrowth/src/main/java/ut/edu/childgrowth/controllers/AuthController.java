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

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        AuthRequest authRequest = new AuthRequest(username, password);
        try {
            AuthResponse authResponse = userService.loginUser(authRequest);

            // Lưu token vào session
            session.setAttribute("token", authResponse.getToken());

            return "redirect:/index"; // vẫn redirect nếu đăng nhập đúng
        } catch (RuntimeException e) {
            // Gửi lỗi và giữ lại giá trị người dùng nhập
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "login"; // không redirect -> load lại form trong cùng view
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
        session.invalidate(); // ✅ Xóa token & session khi logout
        return "redirect:/login";
    }

    @GetMapping("/children-list")
    public String showChildrenList() {
        return "children-list"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/add-child")
    public String showAddChild() {
        return "add-child"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/growth-charts")
    public String showGrowthCharts() {
        return "growth-charts"; // Spring sẽ tìm trong src/main/resources/templates
    }

    @GetMapping("/health-alerts")
    public String showHeathAlerts() {
        return "health-alerts"; // Spring sẽ tìm trong src/main/resources/templates
    }

}
