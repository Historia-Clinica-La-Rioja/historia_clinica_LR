package net.pladema.security.authentication.controller;

import io.swagger.annotations.Api;
import net.pladema.security.authentication.controller.dto.JWTokenDto;
import net.pladema.security.authentication.controller.dto.OauthConfigDto;
import net.pladema.security.authentication.controller.mapper.JWTokenMapper;
import net.pladema.security.authentication.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@Api(value = "Oauth", tags = {"Oauth"})
public class ExternalAuthenticationController {

	private static final Logger LOG = LoggerFactory.getLogger(ExternalAuthenticationController.class);

	private final JWTokenMapper jWTokenMapper;
	private final OauthService oauthService;

	public ExternalAuthenticationController(
			JWTokenMapper jWTokenMapper,
			OauthService oauthService
	) {
		super();
		this.jWTokenMapper = jWTokenMapper;
		this.oauthService = oauthService;
	}

	@GetMapping(value = "/config")
	public ResponseEntity<OauthConfigDto> getPublicConfig() {
		LOG.debug("Oauth get public config");
		return ResponseEntity.ok().body(new OauthConfigDto(oauthService.getLoginUrl(), oauthService.getOauthEnabled()));
	}
	
	@GetMapping(value = "/chaco")
	public ResponseEntity<JWTokenDto> loginChaco(@RequestParam("code") String code) throws Exception {
		LOG.debug("Login chaco with code=> {}", code);
		return ResponseEntity.ok().body(jWTokenMapper.mapNewToken(oauthService.loginChaco(code)));
	}

	@GetMapping(value = "/chaco/redirectUrl")
	public ResponseEntity<String> getChacoRedirectUrl() throws Exception {
		LOG.debug("Get chaco redirect url");
		return ResponseEntity.ok().body(oauthService.getLoginUrl());
	}
	
}