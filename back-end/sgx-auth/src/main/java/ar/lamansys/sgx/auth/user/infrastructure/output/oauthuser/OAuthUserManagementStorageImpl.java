package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser;

import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.auth.user.application.exception.OAuthUserEnumException;
import ar.lamansys.sgx.auth.user.application.exception.OAuthUserException;
import ar.lamansys.sgx.auth.user.domain.user.model.OAuthUserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration.OAuthRestTemplateAuth;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto.OAuthUserBasicData;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto.OAuthUserRepresentationCredentials;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.dto.OAuthUserRepresentationPayload;
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
    public void updateUser(String currentUsername, OAuthUserBo newUserData) {
        log.debug("Input parameter -> currentUsername {}, newUserData {}", currentUsername, newUserData);
        String oAuthUserId = this.getOAuthUserId(currentUsername);
        if (oAuthUserId == null)
            throw new OAuthUserException(OAuthUserEnumException.ERROR_GETTING_USER, "Error obteniendo usuario en el servidor");
        updateUserData(oAuthUserId, newUserData);
    }

    private String getOAuthUserId(String username) {
        log.debug("Input parameter -> username {}", username);
        String url = oAuthWSConfig.getCreateUser()
                + "?username=" + username
                + "&exact=true";
        try {
            ResponseEntity<OAuthUserBasicData[]> response = exchangeGet(url, OAuthUserBasicData[].class);
            return (response.getBody() != null && response.getBody().length > 0) ? response.getBody()[0].getId() : null;
        } catch (Exception e) {
            log.debug("Error getting user in OAuth Server");
        }
        return null;
    }

    private void updateUserData(String oAuthUserId, OAuthUserBo newUserData) {
        log.debug("Input parameters -> oAuthUserId {}, newUserData {}", oAuthUserId, newUserData);
        String url = oAuthWSConfig.getCreateUser()
                + "/" + oAuthUserId;
        try {
            ResponseEntity<Object> response = exchangePut(url, mapToUserCreationPayload(newUserData), Object.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("User updated successfully in OAuth Server");
            }
        } catch (Exception e) {
            log.debug("Error updating user in OAuth Server");
        }

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
            throw new OAuthUserException(OAuthUserEnumException.ERROR_CREATING_USER, "Error creando el usuario en el servidor");
        }

    }

    private OAuthUserRepresentationPayload mapToUserCreationPayload(OAuthUserBo oAuthUserBo) {
        return new OAuthUserRepresentationPayload(oAuthUserBo.getUsername(),
                oAuthUserBo.getFirstName(),
                oAuthUserBo.getLastName(),
                oAuthUserBo.getEmail(),
                "true",
                (oAuthUserBo.getPassword() != null) ? List.of(new OAuthUserRepresentationCredentials("password", oAuthUserBo.getPassword(), false)) : null);
    }

}
