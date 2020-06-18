package net.pladema.security.authentication.service.impl;

import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.pladema.security.authentication.business.OauthUser;
import net.pladema.security.authentication.service.OauthDataService;
import net.pladema.security.configuration.OAuthConfiguration;

@Service
public class OauthChacoDataServiceImpl implements OauthDataService {

	private final Logger logger;
	private final RestTemplate restTemplate;
	private final OAuthConfiguration oAuthConfiguration;

	public OauthChacoDataServiceImpl(
			@Qualifier("baseRestTemplate") RestTemplate restTemplate,
			OAuthConfiguration oAuthConfiguration
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.restTemplate = restTemplate;
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
