package ut.edu.childgrowth.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Hiển thị trang thông tin người dùng
    @GetMapping("/{username}")
    public String getUserProfile(@PathVariable String username, Model model) {
        try {
            UserResponse user = userService.getUserByUsername(username);
            model.addAttribute("user", user);
            return "user-profile"; // Tên file Thymeleaf (user-profile.html)
        } catch (RuntimeException e) {
            model.addAttribute("error", "Người dùng không tồn tại");
            return "error"; // Trang lỗi nếu không tìm thấy
        }
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest());
        return "register"; // Tên file Thymeleaf (register.html)
    }

    // Xử lý đăng ký từ form
    @PostMapping("/register")
    public String registerFromForm(@ModelAttribute("user") UserRegisterRequest request, Model model) {
        try {
            userService.registerUser(request);
            return "redirect:/user/login"; // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Quay lại form nếu có lỗi
        }
    }
}