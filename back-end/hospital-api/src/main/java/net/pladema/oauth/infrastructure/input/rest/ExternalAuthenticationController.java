package net.pladema.oauth.infrastructure.input.rest;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.OauthConfigDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.oauth.application.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@Tag(name = "Oauth", description = "Oauth")
public class ExternalAuthenticationController {

	private static final Logger LOG = LoggerFactory.getLogger(ExternalAuthenticationController.class);

	private final OauthService oauthService;

	public ExternalAuthenticationController(
			OauthService oauthService
	) {
		super();
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
		return ResponseEntity.ok().body(oauthService.login(code));
	}

}