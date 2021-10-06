package net.pladema.oauth.application;

import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.LoginDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.JwtExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import net.pladema.oauth.domain.Documento;
import net.pladema.oauth.domain.OauthDataService;
import net.pladema.oauth.domain.OauthUser;
import net.pladema.oauth.infrastructure.output.service.OAuthConfiguration;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

@Service
public class OauthServiceImpl implements OauthService {

	private final Logger logger;
	private final UserExternalService userExternalService;
	private final JwtExternalService jwtExternalService;
	private final PersonExternalService personService;
	private final RestTemplate restTemplate;
	private final OAuthConfiguration oAuthConfiguration;
	private final OauthDataService oauthDataService;

	public OauthServiceImpl(
			UserExternalService userExternalService,
			JwtExternalService jwtExternalService,
			OAuthConfiguration oAuthConfiguration,
			PersonExternalService personService,
			OauthDataService oauthDataService
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userExternalService = userExternalService;
		this.jwtExternalService = jwtExternalService;
		this.restTemplate = new RestTemplate();
		this.oAuthConfiguration = oAuthConfiguration;
		this.personService = personService;
		this.oauthDataService = oauthDataService;
	}



	public JWTokenDto login(String code) throws URISyntaxException, BadLoginException {
		OauthUser oauthUser = oauthDataService.getUserData(getOauthToken(code));
		LoginDto loginDto = userExternalService.getUser(oauthUser.getCuitCuil())
				.map(user -> {
					userExternalService.updateLoginDate(user.getUsername());
					return new LoginDto(user.getUsername(), user.getPassword());
				})
				.orElse(generateLoginFromNewUser(oauthUser));
		var token = jwtExternalService.login(loginDto);
		logger.debug("RETURN token user -> {}", token.getToken());
		return token;
	}

	private String getOauthToken(String code) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(oAuthConfiguration.getTokenUrl());
		uriBuilder.addParameter("code", code);
		uriBuilder.addParameter("client_id", oAuthConfiguration.getClientId());
		uriBuilder.addParameter("client_secret", oAuthConfiguration.getClientSecret());
		uriBuilder.addParameter("grant_type", "authorization_code");
		uriBuilder.addParameter("redirect_uri", oAuthConfiguration.getRedirectUri());
		logger.debug("GET Oauth Token from url {}", uriBuilder.build());
		return restTemplate.getForObject(uriBuilder.build(), HashMap.class).get("access_token").toString();
	}

	private LoginDto generateLoginFromNewUser(OauthUser oauthUser) {
		String password =  generatePassword(oauthUser);
		String username = oauthUser.getCuitCuil();
		userExternalService.registerUser(username, null, password);
		userExternalService.enableUser(username);
		userExternalService.updateLoginDate(username);
		createPerson(oauthUser);
		return new LoginDto(username, password);
	}

	private void createPerson(OauthUser oauthUser) {
		List<Documento> documents = oauthUser.getTiposDocumentoPersona();
		if (documents.isEmpty())
			return;
		APatientDto result = new APatientDto();
		result.setFirstName(oauthUser.getNombres());
		result.setLastName(oauthUser.getApellidos());
		result.setIdentificationNumber(oauthUser.getTiposDocumentoPersona().get(0).getNumeroDocumento());
		oauthUser.getIdentificationTypeId().ifPresent(result::setIdentificationTypeId);
		oauthUser.getGenderId().ifPresent(result::setGenderId);
		personService.addPerson(result);
	}

	private static String generatePassword(OauthUser oAuthUser) {
		return new StringBuilder(oAuthUser.getCuitCuil()).reverse().toString();
	}

	@Override
	public String getLoginUrl() {
		return oAuthConfiguration.getLoginUrl();
	}

	@Override
	public Boolean getOauthEnabled() {
		return oAuthConfiguration.getEnabled();
	}
}
