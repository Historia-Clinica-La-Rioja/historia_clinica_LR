package net.pladema.oauth.application.ports;

import net.pladema.oauth.domain.OAuthConfigBo;

public interface OAuthConfigurationStorage {

    OAuthConfigBo getConfiguration();

}
