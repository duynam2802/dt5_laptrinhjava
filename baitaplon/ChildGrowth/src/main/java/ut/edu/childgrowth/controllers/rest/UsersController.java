package ut.edu.childgrowth.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.AuthRequest;
import ut.edu.childgrowth.dtos.AuthResponse;
import ut.edu.childgrowth.dtos.UserRegisterRequest;
import ut.edu.childgrowth.dtos.UserResponse;
import ut.edu.childgrowth.dtos.PasswordChangeRequest;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
//    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
//        UserResponse response = userService.registerUser(request);
//        return ResponseEntity.ok(response);
//    }
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        String result = String.valueOf(userService.registerUser(userRegisterRequest));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ut.edu.vn.dms.dtos.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username,
                                                          @RequestHeader("Authorization") String authHeader) {
        // Kiểm tra token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String extractedUsername = jwtUtil.extractUsername(token);
        if (!extractedUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Xác thực token
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody User user,
                                                   @RequestHeader("Authorization") String authHeader) {
        // Kiểm tra token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.updateUser(id, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @RequestBody PasswordChangeRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
        // Kiểm tra token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String result = userService.changePassword(id, request);
        if ("Mật khẩu đã được thay đổi thành công".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}