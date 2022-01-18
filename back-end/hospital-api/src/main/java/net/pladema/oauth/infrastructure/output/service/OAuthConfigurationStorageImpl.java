package net.pladema.oauth.infrastructure.output.service;

import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.oauth.application.ports.OAuthConfigurationStorage;
import net.pladema.oauth.domain.OAuthConfigBo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthConfigurationStorageImpl implements OAuthConfigurationStorage {

    private final OAuthWSConfig oAuthWSConfig;

    @Override
    public OAuthConfigBo getConfiguration() {
        log.debug("No input parameters");
        OAuthConfigBo result = new OAuthConfigBo(
                oAuthWSConfig.getIssuer(),
                oAuthWSConfig.getClientId(),
                oAuthWSConfig.getBaseUrl() + oAuthWSConfig.getLogout(),
                oAuthWSConfig.isEnabled());
        log.debug("Output -> {}", result);
        return result;
    }
}

