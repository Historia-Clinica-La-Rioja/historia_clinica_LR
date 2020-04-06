package net.pladema.user.events.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import net.pladema.emails.service.EmailService;
import net.pladema.security.token.service.TokenService;
import net.pladema.user.events.OnResetPasswordEvent;
import net.pladema.user.repository.entity.User;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

	private final TokenService tokenService;

	private final EmailService emailService;

	public ResetPasswordListener(TokenService tokenService, EmailService emailService) {
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
		String verificationToken = tokenService.generateVerificationToken(user.getId());
		emailService.sendResetPasswordMail(user.getUsername(), verificationToken, user.getId());
	}
}