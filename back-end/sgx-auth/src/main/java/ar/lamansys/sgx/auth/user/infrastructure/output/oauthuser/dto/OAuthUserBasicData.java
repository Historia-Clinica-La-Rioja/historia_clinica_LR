package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OAuthUserBasicData implements Serializable {

    private String id;

    private String username;

    private String enabled;

}
