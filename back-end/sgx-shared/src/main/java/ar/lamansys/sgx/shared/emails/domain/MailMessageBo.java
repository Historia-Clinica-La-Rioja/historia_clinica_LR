package ar.lamansys.sgx.shared.emails.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.core.io.ByteArrayResource;
import java.util.HashMap;

@ToString
@AllArgsConstructor
public class MailMessageBo {
	public final String subject;
	public final String body;
	public final HashMap<String, ByteArrayResource> attachments;

	public MailMessageBo(String subject, String body) {
		this.subject = subject;
		this.body = body;
		attachments = new HashMap<>();
	}
}
