package ut.edu.childgrowth.controllers;

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
            Model model) {

        AuthRequest authRequest = new AuthRequest(username, password);
        try {
            AuthResponse authResponse = userService.loginUser(authRequest);
            // Có thể lưu token vào session nếu muốn sử dụng về sau
            model.addAttribute("token", authResponse.getToken());
            return "redirect:/index";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    

    @GetMapping("/index")
    public String showIndex() {
        return "index";
    }
}
