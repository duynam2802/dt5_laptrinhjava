package ut.edu.childgrowth.dtos;

public class AuthRequest {
    private String username;
    private String password;

    // Constructor mặc định
    public AuthRequest() {}

    // Constructor có tham số
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters và Setters
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
}