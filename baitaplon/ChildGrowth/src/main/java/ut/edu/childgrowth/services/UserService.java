package ut.edu.childgrowth.services;

import ut.edu.childgrowth.controllers.PasswordChangeRequest;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Đăng ký người dùng
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        return userRepository.save(user);
    }

    // Tìm người dùng theo username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Tìm người dùng theo email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Cập nhật thông tin người dùng
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFullName(updatedUser.getFullName());
            user.setNumPhone(updatedUser.getNumPhone());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole()); // Nếu muốn cập nhật quyền
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User không tồn tại!");
        }
    }

    // Xóa người dùng
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User không tồn tại!");
        }
    }

    // Thay đổi mật khẩu
    public boolean changePassword(Long id, PasswordChangeRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getOldPassword())) {
                user.setPassword(request.getNewPassword());
                userRepository.save(user);
                return true;
            }
        }
        return false; // Trả về false nếu mật khẩu cũ không đúng hoặc user không tồn tại
    }
}
