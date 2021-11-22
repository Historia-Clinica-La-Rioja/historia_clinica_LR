package ar.lamansys.sgx.auth.user.infrastructure.events.listeners;

import ar.lamansys.sgx.auth.jwt.application.verificationuser.VerificationTokenService;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.shared.emails.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.auth.user.infrastructure.events.OnResetPasswordEvent;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

	private final VerificationTokenService tokenService;

	private final EmailService emailService;

	public ResetPasswordListener(VerificationTokenService tokenService, EmailService emailService) {
		super();
		this.tokenService = tokenService;
		this.emailService = emailService;
	}

	@Override
	public void onApplicationEvent(OnResetPasswordEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnResetPasswordEvent event) {
		User user = event.getUser();
		String verificationToken = tokenService.generateVerificationToken(user.getId(), user.getUsername());
		emailService.sendResetPasswordMail(user.getUsername(), verificationToken, user.getId());
	}
}