package api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Registration {
    private String email;
    private String password;
    public Registration(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Registration() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}