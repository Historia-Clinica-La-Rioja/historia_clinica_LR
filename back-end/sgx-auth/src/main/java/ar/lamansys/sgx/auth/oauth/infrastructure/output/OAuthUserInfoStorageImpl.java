package ar.lamansys.sgx.auth.oauth.infrastructure.output;

import ar.lamansys.sgx.auth.oauth.application.ports.OAuthUserInfoStorage;
import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.dto.OAuthUserInfoDto;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.mapper.OAuthUserMapper;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OAuthUserInfoStorageImpl extends RestClient implements OAuthUserInfoStorage {

    private final OAuthWSConfig oAuthWSConfig;

    private final OAuthUserMapper oAuthUserMapper;

    public OAuthUserInfoStorageImpl(
			HttpClientConfiguration configuration,
			OAuthWSConfig oAuthWSConfig,
			OAuthUserMapper oAuthUserMapper
	) throws Exception {
        super(
				new RestTemplateSSL(configuration),
				oAuthWSConfig
		);
        this.oAuthWSConfig = oAuthWSConfig;
        this.oAuthUserMapper = oAuthUserMapper;
    }

    @Override
    public Optional<OAuthUserInfoBo> getUserInfo(String accessToken) {

        ResponseEntity<OAuthUserInfoDto> responseEntity = getOAuthUserInfoResponse(accessToken);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            log.debug("Output -> No user info obtained");
            return Optional.empty();
        }
        OAuthUserInfoDto responseDto = responseEntity.getBody();
        Optional<OAuthUserInfoBo> opOAuthUserInfoBo = Optional.of(oAuthUserMapper.fromOAuthUserInfoDto(responseDto));
        log.debug("Output -> {}", opOAuthUserInfoBo);
        return opOAuthUserInfoBo;
    }

    private ResponseEntity<OAuthUserInfoDto> getOAuthUserInfoResponse(String accessToken) {
        String url = oAuthWSConfig.getUserInfo();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        return exchangeGet(url, headers, OAuthUserInfoDto.class);
    }

}
