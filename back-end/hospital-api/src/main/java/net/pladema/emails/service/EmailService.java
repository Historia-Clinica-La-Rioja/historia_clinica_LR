package net.pladema.emails.service;

public interface EmailService {

	public void sendActivationAccountMail(String to, String verificationToken, Integer userId);

	public void sendResetPasswordMail(String to, String verificationToken, Integer userId);

	public void sendContactEmail(String email, String subject, String msg);

}