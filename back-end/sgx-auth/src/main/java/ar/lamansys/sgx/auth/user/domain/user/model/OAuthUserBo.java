package ar.lamansys.sgx.auth.user.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserBo {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    @Override
    public String toString() {
        return "OAuthUserBo{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
