package net.pladema.oauth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.oauth.application.ports.OAuthConfigurationStorage;
import net.pladema.oauth.domain.OAuthConfigBo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetOAuthConfigInfo {

    private final OAuthConfigurationStorage oAuthConfigurationStorage;

    public OAuthConfigBo run(){
        log.debug("No input parameters");
        OAuthConfigBo result = oAuthConfigurationStorage.getConfiguration();
        log.debug("Output -> {}", result);
        return result;
    }

}
