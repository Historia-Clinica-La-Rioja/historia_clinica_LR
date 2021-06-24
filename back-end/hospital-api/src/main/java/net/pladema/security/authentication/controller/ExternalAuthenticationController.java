package net.pladema.security.authentication.controller;

import io.swagger.annotations.Api;
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
	
	@GetMapping(value = "/login")
	public ResponseEntity<JWTokenDto> login(@RequestParam("code") String code) throws Exception {
		LOG.debug("Login oauth with code=> {}", code);
		return ResponseEntity.ok().body(jWTokenMapper.mapNewToken(oauthService.login(code)));
	}

}