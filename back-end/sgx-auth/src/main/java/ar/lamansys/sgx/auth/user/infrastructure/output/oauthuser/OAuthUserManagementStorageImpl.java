package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser;

import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.auth.user.domain.user.model.OAuthUserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration.OAuthRestTemplateAuth;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto.OAuthUserCreationCredentials;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto.OAuthUserCreationPayload;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OAuthUserManagementStorageImpl extends RestClient implements OAuthUserManagementStorage {

    private final OAuthWSConfig oAuthWSConfig;

    public OAuthUserManagementStorageImpl(OAuthRestTemplateAuth restTemplateAuth, OAuthWSConfig oAuthWSConfig) {
        super(restTemplateAuth, oAuthWSConfig);
        this.oAuthWSConfig = oAuthWSConfig;
    }

    @Override
    public void createUser(OAuthUserBo oAuthUserBo) {
        log.debug("Input parameter -> oAuthUserBo {}", oAuthUserBo);
        String url = oAuthWSConfig.getCreateUser();
        try {
            ResponseEntity<Object> response = exchangePost(url, mapToUserCreationPayload(oAuthUserBo), Object.class);
            if (HttpStatus.CREATED.equals(response.getStatusCode())) {
                log.debug("User created successfully in OAuth Server");
            }
        } catch (Exception e) {
            log.debug("Error creating user in OAuth Server");
        }

    }

    private OAuthUserCreationPayload mapToUserCreationPayload(OAuthUserBo oAuthUserBo) {
        return new OAuthUserCreationPayload(oAuthUserBo.getUsername(),
                oAuthUserBo.getFirstName(),
                oAuthUserBo.getLastName(),
                oAuthUserBo.getEmail(),
                "true",
                List.of(new OAuthUserCreationCredentials("password", oAuthUserBo.getPassword(), false)));
    }

}
