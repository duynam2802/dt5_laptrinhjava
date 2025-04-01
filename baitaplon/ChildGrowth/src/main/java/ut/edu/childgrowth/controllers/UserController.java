package ut.edu.childgrowth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

//    // Đăng ký người dùng
//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return userService.registerUser(user);
//    }

    // Đăng ký User mà không thêm Child
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Đăng ký user
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    // Lấy thông tin người dùng
    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setUser_id(id);
        return userService.updateUser(id, user);
    }


    // API thay đổi mật khẩu
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        boolean success = userService.changePassword(id, request);
        if (success) {
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid password.");
        }
    }

    // API quên mật khẩu
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
//        boolean success = userService.sendPasswordResetEmail(request.getEmail());
//        if (success) {
//            return ResponseEntity.ok("Password reset email sent.");
//        } else {
//            return ResponseEntity.badRequest().body("Email not found.");
//        }
//    }

}
