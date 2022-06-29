package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.confirmTwoFactorAuthentication.ConfirmTwoFactorAuthentication;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.generateTwoFactorAuthentication.GenerateTwoFactorAuthentication;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.loggedUserHasTwoFactorAuthenticationEnabled.LoggedUserHasTwoFactorAuthenticationEnabled;
import ar.lamansys.sgx.auth.twoFactorAuthentication.domain.SetTwoFactorAuthenticationBo;
import ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.dto.TwoFactorAuthenticationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/2fa")
@Tag(name = "Two-Factor Authentication", description = "Two-Factor Authentication")
@Slf4j
@RequiredArgsConstructor
public class TwoFactorAuthenticationController {

	private final GenerateTwoFactorAuthentication generateTwoFactorAuthentication;
	private final ConfirmTwoFactorAuthentication confirmTwoFactorAuthentication;
	private final LoggedUserHasTwoFactorAuthenticationEnabled loggedUserHasTwoFactorAuthenticationEnabled;

	@PostMapping()
	public ResponseEntity<TwoFactorAuthenticationDto> generateTwoFactorAuthenticationCodes() {
		log.debug("Generate Two-factor Authentication codes");
		SetTwoFactorAuthenticationBo authenticationBo = generateTwoFactorAuthentication.run();
		TwoFactorAuthenticationDto result = new TwoFactorAuthenticationDto(authenticationBo.getSharedSecretBarCode(), authenticationBo.getSharedSecret());
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/confirm")
	public ResponseEntity<Boolean> confirmTwoFactorAuthentication(@RequestParam(name = "code") String code) {
		log.debug("Confirm Two-factor Authentication");
		Boolean result = confirmTwoFactorAuthentication.run(code);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/enabled-for-logged-user")
	public ResponseEntity<Boolean> userHasTwoFactorAuthenticationEnabled() {
		log.debug("Confirm Two-factor Authentication");
		Boolean result = loggedUserHasTwoFactorAuthenticationEnabled.run();
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

}

