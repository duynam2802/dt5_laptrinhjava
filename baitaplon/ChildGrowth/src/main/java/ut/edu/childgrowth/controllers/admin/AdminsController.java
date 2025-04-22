package ut.edu.childgrowth.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.admin.*;
import ut.edu.childgrowth.services.AdminService;
import ut.edu.childgrowth.services.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminsController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    // Đăng ký admin
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> register(@RequestBody AdminRegisterRequest request) {
        adminService.register(request);
        return ResponseEntity.ok("Đăng ký admin thành công!");
    }

    // Đăng nhập admin
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AdminLoginRequest request) {
        AuthResponse response = adminService.login(request);
        return ResponseEntity.ok(response);
    }

    // Tạo tài khoản User
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserRegisterRequest request) {
        adminService.checkAuthorization(authorizationHeader);  // Kiểm tra token trước
        String result = userService.registerUser(request);
        return ResponseEntity.ok("Tạo tài khoản user thành công!");
    }

    // Tạo tài khoản Doctor
//    @PostMapping("/create-doctor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> createDoctor(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateDoctorRequest request) {
//        adminService.checkAuthorization(authorizationHeader);  // Kiểm tra token trước
//        adminService.createDoctor(request);
//        return ResponseEntity.ok("Tạo tài khoản doctor thành công!");
//    }

    // Tạo tài khoản Admin khác
    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public <CreateAdminRequest> ResponseEntity<String> createAdmin(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateAdminRequest request) {
        adminService.checkAuthorization(authorizationHeader);  // Kiểm tra token trước
        adminService.register((AdminRegisterRequest) request);
        return ResponseEntity.ok("Tạo tài khoản admin thành công!");
    }
}
