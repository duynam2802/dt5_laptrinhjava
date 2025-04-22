package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.dtos.admin.AdminLoginRequest;
import ut.edu.childgrowth.dtos.admin.AdminRegisterRequest;
import ut.edu.childgrowth.jwt.JwtTokenProvider;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.Admin;
import ut.edu.childgrowth.repositories.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    public void checkAuthorization(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid token.");
        }
    }

    public void register(AdminRegisterRequest request) {
        if (adminRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (adminRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setFullName(request.getFullName());
        admin.setEmail(request.getEmail());
        admin.setActive(true);

        adminRepo.save(admin);
    }

    public AuthResponse login(AdminLoginRequest request) {
        Admin admin = adminRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu");
        }

        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        return new AuthResponse(token);
    }
}
