package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OAuthUserRepresentationCredentials implements Serializable {

    private String type;

    private String value;

    private boolean temporary;

}


