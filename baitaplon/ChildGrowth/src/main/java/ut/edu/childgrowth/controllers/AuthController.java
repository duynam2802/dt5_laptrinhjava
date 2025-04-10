package ut.edu.childgrowth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về login.html trong templates/
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        // Kiểm tra tài khoản - ở đây là ví dụ đơn giản
        if ("admin".equals(username) && "123".equals(password)) {
            return "redirect:/index"; // Chuyển đến trang index.html (trong templates/)
        }

        // Nếu đăng nhập sai
        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        return "login";
    }

    @GetMapping("/index")
    public String showIndex() {
        return "index"; // Trả về index.html từ templates/
    }
}
