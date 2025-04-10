package ut.edu.childgrowth.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ut.edu.childgrowth.dtos.AuthRequest;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // trả về login.html
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,         // ✅ thêm vào để lưu session
            Model model) {

        AuthRequest authRequest = new AuthRequest(username, password);
        try {
            AuthResponse authResponse = userService.loginUser(authRequest);

            // ✅ Lưu token vào session để dùng lại
            session.setAttribute("token", authResponse.getToken());

            return "redirect:/index";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
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
        session.invalidate(); // ✅ Xóa token & session khi logout
        return "redirect:/login";
    }
}
