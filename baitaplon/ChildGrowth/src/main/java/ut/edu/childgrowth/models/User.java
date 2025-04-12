package ut.edu.childgrowth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//THÀNH VIÊN

@Entity
@Table(name = "users")
public class User {

    @Id // Khoá chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Setter
    @Getter
    @Column(name = "numphone",nullable = false, length = 10)
    private String numphone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Child> children; // Quan hệ với Child, một User có thể quản lý nhiều Children

    public User() {
    }

    public User(String username, String email, String password, String fullName, UserRole role, String numPhone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.numphone = numPhone;
    }

    // Getters và setters
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long id) {
        this.user_id = id;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    public void setRole(String role) {
        this.role = UserRole.valueOf(role);
    }

    public String getUsername() {
        return username;
    }

    public String getNumphone() {
        return numphone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumphone(String numPhone) {
        this.numphone = numPhone;
    }



    // Getter và setter cho danh sách trẻ em
    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }


}
