package net.pladema.oauth.infrastructure.input.rest;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.OauthConfigDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.oauth.application.GetOAuthConfigInfo;
import net.pladema.oauth.application.OauthService;
import net.pladema.oauth.domain.OAuthConfigBo;
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

	private final GetOAuthConfigInfo getOAuthConfigInfo;

	public ExternalAuthenticationController(
			OauthService oauthService,
			GetOAuthConfigInfo getOAuthConfigInfo) {
		super();
		this.oauthService = oauthService;
		this.getOAuthConfigInfo = getOAuthConfigInfo;
	}

	@GetMapping(value = "/config")
	public ResponseEntity<OauthConfigDto> getPublicConfig() {
		LOG.debug("OAuth get public config");
		OAuthConfigBo oAuthConfigBo = getOAuthConfigInfo.run();
		return ResponseEntity.ok().body(new OauthConfigDto(oAuthConfigBo.getIssuerUrl(), oAuthConfigBo.getClientId(), oAuthConfigBo.getLogoutUrl(), oAuthConfigBo.isEnabled()));
	}
	
	@GetMapping(value = "/login")
	public ResponseEntity<JWTokenDto> login(@RequestParam("code") String code) throws Exception {
		LOG.debug("Login oauth with code=> {}", code);
		return ResponseEntity.ok().body(oauthService.login(code));
	}

}