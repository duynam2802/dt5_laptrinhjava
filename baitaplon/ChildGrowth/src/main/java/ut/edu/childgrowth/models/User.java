package ut.edu.childgrowth.models;
import jakarta.persistence.*;
import java.util.*;
import ut.edu.childgrowth.models.User;

@Entity
@Table (name = "users")
public class User {
    @Id //Khoachinh
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, length = 10)
    private String numPhone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() {
    }

    public User(String email, String password, String fullName, Role role, String numPhone) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.numPhone = numPhone;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getNumPhone() {
        return numPhone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumPhone(String numPhone) {
        this.numPhone = numPhone;
    }
}

