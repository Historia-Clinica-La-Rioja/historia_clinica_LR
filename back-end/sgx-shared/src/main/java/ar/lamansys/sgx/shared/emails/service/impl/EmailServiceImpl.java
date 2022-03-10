package ar.lamansys.sgx.shared.emails.service.impl;

import ar.lamansys.sgx.shared.emails.domain.Mail;
import ar.lamansys.sgx.shared.emails.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${mail.from}")
	protected String mainMail;

	@Value("${server.servlet.context-path}")
	protected String apiDomain;

	@Value("${server.port}")
	protected String apiPort;

	@Value("${server.servlet.context-path}")
	protected String apiContext;

	@Value("${app.mail.activate}")
	private boolean activatedEmailSending;

	public final JavaMailSender emailSender;

	public final TemplateEngine templateEngine;

	public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
		super();
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendActivationAccountMail(String to, String verificationToken, Integer userId) {
		String subject = "Registration Confirmation";
		String verificationLink = apiDomain + ":" + apiPort + apiContext + "/users/" + userId + "/enable"
				+ "?token=" + verificationToken;
		sendEmail(to, subject, verificationLink, "email-activation");
	}

	@Override
	public void sendResetPasswordMail(String to, String verificationToken, Integer userId) {
		String subject = "Reset password";
		String verificationLink = apiDomain + ":" + apiPort + apiContext + "/passwords/reset" + "?token="
				+ verificationToken;
		sendEmail(to, subject, verificationLink, "email-resetpassword");

	}

	protected void sendEmail(String to, String subject, String verificationLink, String template) {
		Mail mail = loadDataEmail(to, subject, verificationLink);
		try {
			sendTemplatedEmail(mail, template);
			LOG.debug("Activation link resent -> {}", to);
		} catch (MailException | MessagingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected Mail loadDataEmail(String to, String subject, String verificationLink) {
		Mail mail = new Mail();
		mail.setFrom(mainMail);
		mail.setTo(to);
		mail.setSubject(subject);

		Map<String, Object> model = new HashMap<>();
		model.put("verificationlink", verificationLink);
		mail.setModel(model);
		return mail;
	}

	private void sendTemplatedEmail(Mail mail, String template) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(mail.getModel());
		String html = templateEngine.process(template, context);

		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		if (activatedEmailSending)
			emailSender.send(message);
	}

	@Override
	public void sendContactEmail(String email, String subject, String msg) {
		try {
			sendBasicEmail(email, subject, msg);
		} catch (MailException | MessagingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void sendBasicEmail(String email, String subject, String msg) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		helper.setTo(mainMail);
		helper.setSubject(subject);
		helper.setFrom(email);
		helper.setText(msg);
		if (activatedEmailSending)
			emailSender.send(message);
	}

}