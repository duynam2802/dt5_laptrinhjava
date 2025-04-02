package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.dtos.AuthRequest;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.dtos.PasswordChangeRequest;
import ut.edu.childgrowth.models.User;
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

    // Đăng ký người dùng
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Lưu mật khẩu dạng plaintext
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullname());
        user.setNumPhone(request.getNumPhone());

        user = userRepository.save(user);
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    // Đăng nhập người dùng
    public AuthResponse loginUser(AuthRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        User user = userOptional.get();
        if (!request.getPassword().equals(user.getPassword())) { // So sánh mật khẩu dạng plaintext
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    // Tìm người dùng theo username
    public UserResponse getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        User user = userOptional.get();
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    // Tìm người dùng theo email
    public UserResponse getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        User user = userOptional.get();
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail());
    }

    // Cập nhật thông tin người dùng
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

    // Xóa người dùng
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }
        userRepository.deleteById(id);
    }

    // Thay đổi mật khẩu
    public String changePassword(Long id, PasswordChangeRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return "Người dùng không tồn tại";
        }

        User user = userOptional.get();
        if (!request.getOldPassword().equals(user.getPassword())) { // So sánh mật khẩu dạng plaintext
            return "Mật khẩu cũ không chính xác";
        }

        user.setPassword(request.getNewPassword()); // Lưu mật khẩu mới dạng plaintext
        userRepository.save(user);
        return "Mật khẩu đã được thay đổi thành công";
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