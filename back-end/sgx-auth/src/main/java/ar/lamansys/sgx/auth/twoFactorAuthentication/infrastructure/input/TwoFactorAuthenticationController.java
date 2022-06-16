package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.GenerateTwoFactorAuthentication;
import ar.lamansys.sgx.auth.twoFactorAuthentication.domain.SetTwoFactorAuthenticationBo;
import ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.dto.TwoFactorAuthenticationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/2fa")
@Tag(name = "Two-Factor Authentication", description = "Two-Factor Authentication")
@Slf4j
@RequiredArgsConstructor
public class TwoFactorAuthenticationController {

	private final GenerateTwoFactorAuthentication generateTwoFactorAuthentication;

	@PostMapping()
	public ResponseEntity<TwoFactorAuthenticationDto> setTwoFactorAuthentication() {
		log.debug("Set Two-factor Authentication");
		SetTwoFactorAuthenticationBo authenticationBo = generateTwoFactorAuthentication.run();
		TwoFactorAuthenticationDto result = new TwoFactorAuthenticationDto(authenticationBo.getSharedSecretBarCode(), authenticationBo.getSharedSecret());
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}

