package ar.lamansys.sgx.shared.emails.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.notification.mail")
public class EmailConfiguration {
	private String from;
	private String fromFullname;
	private String replyTo;
}
