package ut.edu.childgrowth.dtos;

import lombok.Data;
import lombok.Getter;

@Data
public class UserRegisterRequest {
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String role;
    private String email;
    @Getter
    private String numPhone;
    @Getter
    private String fullname;

//    public String getFullname() {
//        return fullName;
//    }
}