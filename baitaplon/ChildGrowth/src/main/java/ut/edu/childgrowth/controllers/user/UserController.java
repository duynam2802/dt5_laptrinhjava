package ut.edu.childgrowth.controllers.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

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

//
// Hiển thị trang chỉnh sửa thông tin
@GetMapping("/profile/update")
public String showProfile(Model model, HttpSession session) {
    // ✅ Lấy token từ session
    String token = (String) session.getAttribute("token");

    if (token == null) {
        return "redirect:/login";
    }

    String username = jwtUtil.extractUsername(token);

    User user = userService.findByUsername(username);
    model.addAttribute("user", user);
    return "edit-profile";
}

    // Xử lý cập nhật thông tin
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                Model model,
                                HttpSession session) {
        try {
            // ✅ Lấy token từ session
            String token = (String) session.getAttribute("token");
            if (token == null) {
                return "redirect:/login";
            }

            String username = jwtUtil.extractUsername(token);
            User currentUser = userService.findByUsername(username);

            if (!updatedUser.getUser_id().equals(currentUser.getUser_id())) {
                model.addAttribute("errorMessage", "Bạn không có quyền chỉnh sửa người dùng này!");
                return "edit-profile";
            }

            UserResponse response = userService.updateUser(currentUser.getUser_id(), updatedUser);

            model.addAttribute("user", updatedUser);
            model.addAttribute("successMessage", response.getMessage());
            return "edit-profile";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "edit-profile";
        }
    }

}

