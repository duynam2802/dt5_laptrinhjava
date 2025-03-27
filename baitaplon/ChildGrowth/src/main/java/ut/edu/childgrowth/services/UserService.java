package ut.edu.childgrowth.services;

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
        return userRepository.save(user);
    }

    // Tìm người dùng
    public Optional<User> getUserByEmail(String username) {
        return userRepository.findByUsername(username);
    }

    // Cập nhật thông tin người dùng
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user;
        }
        return userRepository.findByUsername(username); // Nếu dùng fullName làm username
    }
}
