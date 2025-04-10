package ut.edu.childgrowth.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.services.UserService;

@Controller
@RequestMapping("/users")
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
    public String processRegister(@ModelAttribute("user") UserRegisterRequest userDTO,
                                  @RequestParam("repeatPassword") String repeatPassword,
                                  Model model) {

        if (!userDTO.getPassword().equals(repeatPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp!");
            return "register";
        }

        try {
            userService.registerUser(userDTO);  // GỌI LƯU
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tạo tài khoản: " + e.getMessage());
            return "register";
        }
    }

}