package net.pladema.security.authentication.service.impl;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;
import net.pladema.security.authentication.business.Documento;
import net.pladema.security.authentication.business.OauthUser;
import net.pladema.security.authentication.service.OauthService;
import net.pladema.security.configuration.OAuthConfiguration;
import net.pladema.security.token.service.TokenService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.Login;
import net.pladema.user.repository.entity.User;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;

@Service
public class OauthServiceImpl implements OauthService {

	private final Logger logger;
	private final TokenService tokenService;
	private final UserService userService;
	private final UserPasswordService userPasswordService;
	private final PersonService personService;
	private final RestTemplate restTemplate;
	private final OAuthConfiguration oAuthConfiguration;

	public OauthServiceImpl(
			TokenService tokenService,
			UserService userService,
			@Qualifier("baseRestTemplate") RestTemplate restTemplate,
			OAuthConfiguration oAuthConfiguration,
			PersonService personService,
			UserPasswordService userPasswordService
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.tokenService = tokenService;
		this.userService = userService;
		this.restTemplate = restTemplate;
		this.oAuthConfiguration = oAuthConfiguration;
		this.personService = personService;
		this.userPasswordService = userPasswordService;
	}

	public String getOauthToken(String code) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(oAuthConfiguration.getTokenUrl());
		uriBuilder.addParameter("code", code);
		uriBuilder.addParameter("client_id", oAuthConfiguration.getClientId());
		uriBuilder.addParameter("client_secret", oAuthConfiguration.getClientSecret());
		uriBuilder.addParameter("grant_type", "authorization_code");
		uriBuilder.addParameter("redirect_uri", oAuthConfiguration.getRedirectUri());
		logger.debug("GET Oauth Token from url {}", uriBuilder.build());
		return restTemplate.getForObject(uriBuilder.build(), HashMap.class).get("access_token").toString();
	}

	public OauthUser getApiDataChaco(String token) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(oAuthConfiguration.getApiData());
		HttpHeaders headers = new HttpHeaders();
		headers.set(oAuthConfiguration.getTokenHeader(), "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		logger.debug("GET oauth data person");
		return restTemplate.exchange(uriBuilder.build(), HttpMethod.GET, entity, OauthUser.class).getBody();
	}

	public JWToken loginChaco(String code) throws Exception {
		OauthUser oauthUser = getApiDataChaco(getOauthToken(code));
		Optional<User> optionalUser = userService.getUser(oauthUser.getCuitCuil());
		if (!optionalUser.isPresent()) {
			User user = new User();
			Optional<Person> optPerson = createPerson(oauthUser);
			if (optPerson.isPresent()){
				Person p = optPerson.get();
				logger.debug("SAVING person -> {}", p);
				p = personService.addPerson(p);
				user.setPersonId(p.getId());
			}
			user.setUsername(oauthUser.getCuitCuil());
			user = userService.addUser(user);
			userService.updateLoginDate(user.getId());
			logger.debug("SAVING oauth user -> {}", user);
			userPasswordService.addPassword(user, generatePassword(oauthUser));
		} else {
			userService.updateLoginDate(optionalUser.get().getId());
		}

		JWToken token = tokenService.generateToken(new Login(oauthUser.getCuitCuil(), generatePassword(oauthUser)));
		logger.debug("RETURN token user -> {}", token.getToken());
		return token;
	}
	
	private static String generatePassword(OauthUser oAuthUser) {
		return new StringBuilder(oAuthUser.getCuitCuil()).reverse().toString();
	}

	private Optional<Person> createPerson(OauthUser oauthUser) {
		List<Documento> documentos = oauthUser.getTiposDocumentoPersona();
		if (!documentos.isEmpty()) {
			Person p = new Person();
			p.setFirstName(oauthUser.getNombres());
			p.setLastName(oauthUser.getApellidos());
			p.setIdentificationNumber(oauthUser.getTiposDocumentoPersona().get(0).getNumeroDocumento());
			oauthUser.getIdentificationTypeId().ifPresent(id -> p.setIdentificationTypeId(id));
			oauthUser.getGenderId().ifPresent(genderId -> p.setGenderId(genderId));
			return Optional.of(p);
		}
		return Optional.empty();
	}

	@Override
	public String getRedirectUrl() {
		return oAuthConfiguration.getLoginUrl();
	}
}
