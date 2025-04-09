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
    
public String registerUser(UserRegisterRequest userRegisterRequest) {
    if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
        return "Username đã tồn tại!";
    }

    User user = new User();
    user.setUsername(userRegisterRequest.getUsername());
    user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
    user.setRole(userRegisterRequest.getRole());
    user.setEmail(userRegisterRequest.getEmail());
    user.setFullName(userRegisterRequest.getFullname());
    user.setNumphone(userRegisterRequest.getNumPhone());

    userRepository.save(user);
    return "Tạo tài khoản thành công!";
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

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name()); // Tạo JWT token
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
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail(), user.getFullName());
    }

    public UserResponse getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        return new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail(), user.getFullName());

    }

    // Cập nhật thông tin user
    public UserResponse updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại!");
        }

        User user = existingUserOptional.get();

        // Cập nhật nếu có fullName
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }

        // Cập nhật nếu có số điện thoại
        if (updatedUser.getNumphone() != null) {
            user.setNumphone(updatedUser.getNumphone());
        }

        // Cập nhật nếu có email và email đó chưa được dùng
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }
            user.setEmail(updatedUser.getEmail());
        }

        //  cập nhật role nếu cần
        // if (updatedUser.getRole() != null) {
        //     user.setRole(updatedUser.getRole());
        // }

        user = userRepository.save(user);

        return new UserResponse(
                user.getUser_id(),
                user.getUsername(),
                user.getFullName(),
                user.getNumphone(),
                user.getEmail(),
                "Đã thay đổi thành công"
        );
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

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
    }
}