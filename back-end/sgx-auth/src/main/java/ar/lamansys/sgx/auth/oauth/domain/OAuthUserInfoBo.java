package ar.lamansys.sgx.auth.oauth.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OAuthUserInfoBo {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

}
