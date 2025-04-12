package ut.edu.childgrowth.dtos;

import java.util.Map;

public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String numphone;
    private String email;
    private String message;

    public UserResponse() {}


    public UserResponse(Long id, String username, String email, String numphone, String fullName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.numphone = numphone;
        this.username = fullName;
    }

    public UserResponse(Long id, String username, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.email = email;
//        this.numphone = numphone;
        this.username = fullName;
    }

    public UserResponse(Long id, String username, String fullName, String numphone, String email, String message) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.numphone = numphone;
        this.email = email;
        this.message = message;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumphone() {
        return numphone;
    }

    public void setNumphone(String numphone) {
        this.numphone = numphone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Long getUserId() {
        return id;
    }
}