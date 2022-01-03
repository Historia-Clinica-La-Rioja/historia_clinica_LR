package net.pladema.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OAuthConfigBo {

    private String issuerUrl;

    private String clientId;

    private boolean enabled;

}
