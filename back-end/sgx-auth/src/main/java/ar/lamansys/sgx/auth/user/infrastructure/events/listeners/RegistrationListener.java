package ar.lamansys.sgx.auth.user.infrastructure.events.listeners;

import ar.lamansys.sgx.auth.jwt.application.verificationuser.VerificationTokenService;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.shared.emails.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.auth.user.infrastructure.events.OnRegistrationCompleteEvent;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private final VerificationTokenService tokenService;

	private final EmailService emailService;

	public RegistrationListener(VerificationTokenService tokenService, EmailService emailService) {
		super();
		this.tokenService = tokenService;
		this.emailService = emailService;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String verificationToken = tokenService.generateVerificationToken(user.getId(), user.getUsername());
		emailService.sendActivationAccountMail(user.getUsername(), verificationToken, user.getId());
	}
}