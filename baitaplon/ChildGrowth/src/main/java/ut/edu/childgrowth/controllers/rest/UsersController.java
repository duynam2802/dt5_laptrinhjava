package ut.edu.childgrowth.controllers.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ut.edu.childgrowth.dtos.*;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.services.ChildService;
import ut.edu.childgrowth.services.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users") // Uncomment if you want to use this base path
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChildService childService; // Added to handle child-related operations

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Existing endpoints (register, login, getUserByUsername, updateUser, changePassword) remain unchanged
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        String result = String.valueOf(userService.registerUser(userRegisterRequest));
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username,
                                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String extractedUsername = jwtUtil.extractUsername(token);
        if (!extractedUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update_/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody User user,
                                                   @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(usernameFromToken);
        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User targetUser = userService.findById(id);
        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!targetUser.getUsername().equals(usernameFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        UserResponse response = userService.updateUser(id, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @RequestBody PasswordChangeRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
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

    // New endpoint to add a child
//    @PostMapping("/{user_id}/children/add")
//    public ResponseEntity<?> addChild(
//            @PathVariable Long user_id,
//            @Valid @RequestBody Child child,
//            @RequestHeader("Authorization") String authHeader) {
//
//        // Validate token
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
//        }
//
//        String token = authHeader.substring(7);
//        String usernameFromToken = jwtUtil.extractUsername(token);
//
//        // Load user details and validate token
//        UserDetails userDetails = userService.loadUserByUsername(usernameFromToken);
//        if (!jwtUtil.validateToken(token, userDetails)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//
//        // Verify the user_id matches the authenticated user
//        User user = userService.findById(user_id);
//        if (!user.getUsername().equals(usernameFromToken)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add a child for this user");
//        }
//
//        try {
//            // Set the user for the child
//            child.setUser(user);
//
//            // Register the child using ChildService
//            Child registeredChild = childService.registerChild(child);
//            return ResponseEntity.ok(registeredChild);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding child");
//        }
//    }
}