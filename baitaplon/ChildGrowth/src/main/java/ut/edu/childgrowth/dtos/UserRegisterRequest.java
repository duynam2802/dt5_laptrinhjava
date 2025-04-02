package ut.edu.childgrowth.dtos;

public class UserRegisterRequest {
    public String getFullname;
    private String username;
    private String password;
    private String email; // Có thể thêm các trường khác nếu cần
    private String getNumPhone;

    public UserRegisterRequest() {}

    public UserRegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getGetFullname() {
        return getFullname;
    }

    public String getGetNumPhone() {
        return getNumPhone;
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
        return getFullname;
    }

    public String getNumPhone() {
        return getNumPhone;
    }
}