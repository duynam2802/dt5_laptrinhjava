package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.dtos.AuthRequest;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.dtos.PasswordChangeRequest;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.models.UserRole;
import ut.edu.childgrowth.repositories.UserRepository;
import ut.edu.childgrowth.jwt.JwtUtil;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu bằng BCrypt
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullname());
        user.setNumPhone(request.getNumPhone());
        user.setRole(UserRole.USER); // Gán mặc định role là USER

        user = userRepository.save(user);
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    public AuthResponse loginUser(AuthRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // So sánh mật khẩu
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        String token = jwtUtil.generateToken(user.getUsername()); // Tạo JWT token
        return new AuthResponse(token);
    }

    public String changePassword(Long id, PasswordChangeRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return "Người dùng không tồn tại";
        }User user = userOptional.get();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) { // So sánh mật khẩu cũ
            return "Mật khẩu cũ không chính xác";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword())); // Mã hóa mật khẩu mới
        userRepository.save(user);
        return "Mật khẩu đã được thay đổi thành công";
    }

    // Các phương thức khác giữ nguyên
    public UserResponse getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    public UserResponse getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    public UserResponse updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }

        User user = existingUserOptional.get();
        user.setFullName(updatedUser.getFullName());
        user.setNumPhone(updatedUser.getNumPhone());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());

        if (!user.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        user = userRepository.save(user);
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Người dùng không tồn tại: " + username);
        }

        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}