package net.pladema.security.authentication.controller;

import javax.validation.Valid;

import net.pladema.sgx.recaptcha.service.ICaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.security.authentication.controller.dto.JWTokenDto;
import net.pladema.security.authentication.controller.dto.LoginDto;
import net.pladema.security.authentication.controller.mapper.JWTokenMapper;
import net.pladema.security.authentication.controller.mapper.LoginMapper;
import net.pladema.security.authentication.service.AuthenticationService;
import net.pladema.security.token.service.domain.JWToken;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/auth")
@Api(value = "Authorization", tags = { "Authorization" })
public class AuthenticationController {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

	private final AuthenticationService authenticationService;
	
	private final LoginMapper loginMapper;
	
	private final JWTokenMapper jWTokenMapper;

	private final ICaptchaService captchaService;
	
	public AuthenticationController(AuthenticationService authenticationService,
									LoginMapper loginMapper,
									JWTokenMapper jWTokenMapper,
									ICaptchaService iCaptchaService) {
		super();
		this.authenticationService = authenticationService;
		this.loginMapper = loginMapper;
		this.jWTokenMapper = jWTokenMapper;
		this.captchaService = iCaptchaService;
	}

	@PostMapping
	public ResponseEntity<JWTokenDto> login(
			@Valid @RequestBody LoginDto loginDto,
			@RequestHeader("Origin") String frontUrl,
			@RequestHeader(value = "recaptcha", required = false) String recaptcha) throws URISyntaxException {
		if(captchaService.isRecaptchaEnable()){
			captchaService.validRecaptcha(frontUrl, recaptcha);
		}
		LOG.debug("{}", "Login valid");
		JWToken resultToken = authenticationService.login(loginMapper.mapLogin(loginDto));
		LOG.debug("{}", "Generated token");
		return ResponseEntity.ok().body(jWTokenMapper.mapNewToken(resultToken));
	}	

}