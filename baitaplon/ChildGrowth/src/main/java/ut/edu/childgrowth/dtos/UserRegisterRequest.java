package ut.edu.childgrowth.dtos;

import jakarta.validation.constraints.NotNull;

public class UserRegisterRequest {

    @NotNull(message = "Username không được để trống")
    private String username;

    @NotNull(message = "Password không được để trống")
    private String password;

    @NotNull(message = "Email không được để trống")
    private String email;

    @NotNull(message = "Full name không được để trống")
    private String fullname;

    private String numPhone; // Không bắt buộc, nên không cần @NotNull

    // Constructor mặc định
    public UserRegisterRequest() {}

    // Constructor với các trường bắt buộc
    public UserRegisterRequest(String username, String password, String email, String fullname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNumPhone() {
        return numPhone;
    }

    public void setNumPhone(String numPhone) {
        this.numPhone = numPhone;
    }
}