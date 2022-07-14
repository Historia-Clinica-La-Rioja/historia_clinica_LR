package ar.lamansys.sgx.shared.emails.application;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.emails.configuration.EmailConfiguration;
import ar.lamansys.sgx.shared.emails.domain.EmailMessageBo;
import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.notifications.application.NotificationChannel;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailNotificationChannel implements NotificationChannel<MailMessageBo> {
	private final EmailConfiguration configuration;
	private final JavaMailSender emailSender;

	public EmailNotificationChannel(
			EmailConfiguration emailConfiguration,
			JavaMailSender mailSender
	) {
		this.configuration = emailConfiguration;
		this.emailSender = mailSender;
	}

	private void sendImpl(EmailMessageBo emailMessage) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name()
		);

		helper.setFrom(configuration.getFrom(), configuration.getFromFullname());
		helper.setTo(new InternetAddress(emailMessage.to, emailMessage.toFullname));
		helper.setReplyTo(configuration.getReplyTo());

		helper.setSubject(emailMessage.subject);
		helper.setText(emailMessage.html, true);

		emailSender.send(message);
	}

	@Override
	public void send(RecipientBo recipient, MailMessageBo message) {
		log.debug("Sending email to <{}> '{}': {}", recipient.email, message.subject, message.body);
		try {
			sendImpl(new EmailMessageBo(
					recipient.email,
					fullname(recipient),
					message.subject,
					message.body
			));
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("Sending email fail: {}", e.getMessage(), e);
		}
	}

	private static String fullname(RecipientBo recipient) {
		if (recipient == null) return "";
		return String.format("%s %s", recipient.firstname, recipient.lastname).trim();
	}
}
