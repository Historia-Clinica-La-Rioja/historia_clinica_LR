package net.pladema.oauth.infrastructure.input.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.jwt.application.cookie.CookieService;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.OauthConfigDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.oauth.application.GetOAuthConfigInfo;
import net.pladema.oauth.application.OauthService;
import net.pladema.oauth.domain.OAuthConfigBo;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/oauth")
@Tag(name = "Oauth", description = "Oauth")
public class ExternalAuthenticationController {

	private final OauthService oauthService;

	private final GetOAuthConfigInfo getOAuthConfigInfo;

	private final CookieService cookieService;

	@GetMapping(value = "/config")
	public ResponseEntity<OauthConfigDto> getPublicConfig() {
		log.debug("OAuth get public config");
		OAuthConfigBo oAuthConfigBo = getOAuthConfigInfo.run();
		return ResponseEntity.ok().body(new OauthConfigDto(oAuthConfigBo.getIssuerUrl(), oAuthConfigBo.getClientId(), oAuthConfigBo.getLogoutUrl(), oAuthConfigBo.isEnabled()));
	}
	
	@GetMapping(value = "/login")
	public ResponseEntity<JWTokenDto> login(@RequestParam("code") String code) throws Exception {
		log.debug("Login oauth with code=> {}", code);
		JWTokenDto resultToken = oauthService.login(code);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookieService.tokenCookieHeader(resultToken.token))
				.header(HttpHeaders.SET_COOKIE, cookieService.refreshTokenCookieHeader(resultToken.refreshToken))
				.body(resultToken);
	}

}