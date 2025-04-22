package ut.edu.childgrowth.dtos.admin;

public class AdminRegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public CharSequence getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}


