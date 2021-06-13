package net.pladema.user.events.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.emails.service.EmailService;
import net.pladema.security.token.service.TokenService;
import net.pladema.user.events.OnRegistrationCompleteEvent;
import net.pladema.user.repository.entity.User;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private final TokenService tokenService;

	private final EmailService emailService;

	public RegistrationListener(TokenService tokenService, EmailService emailService) {
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
		String verificationToken = tokenService.generateVerificationToken(user.getId());
		emailService.sendActivationAccountMail(user.getUsername(), verificationToken, user.getId());
	}
}