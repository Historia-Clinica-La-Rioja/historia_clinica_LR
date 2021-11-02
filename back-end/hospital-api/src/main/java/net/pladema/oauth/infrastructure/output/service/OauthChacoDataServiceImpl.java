package net.pladema.oauth.infrastructure.output.service;

import net.pladema.oauth.domain.OauthDataService;
import net.pladema.oauth.domain.OauthUser;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;


@Service
public class OauthChacoDataServiceImpl implements OauthDataService {

	private final Logger logger;
	private final RestTemplate restTemplate;
	private final OAuthConfiguration oAuthConfiguration;

	public OauthChacoDataServiceImpl(
			OAuthConfiguration oAuthConfiguration
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.restTemplate = new RestTemplate();
		this.oAuthConfiguration = oAuthConfiguration;
	}

	public OauthUser getUserData(String token) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(oAuthConfiguration.getApiData());
		HttpHeaders headers = new HttpHeaders();
		headers.set(oAuthConfiguration.getTokenHeader(), "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		logger.debug("GET oauth data person");
		return restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, OauthUser.class).getBody();
	}

}
