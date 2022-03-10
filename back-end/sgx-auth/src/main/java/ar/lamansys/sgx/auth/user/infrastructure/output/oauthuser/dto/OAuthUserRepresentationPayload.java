package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OAuthUserRepresentationPayload implements Serializable {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String enabled;

    private List<OAuthUserRepresentationCredentials> credentials;

}
