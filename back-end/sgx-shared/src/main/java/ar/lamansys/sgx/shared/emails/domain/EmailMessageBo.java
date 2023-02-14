package ar.lamansys.sgx.shared.emails.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.core.io.ByteArrayResource;
import java.util.HashMap;

@AllArgsConstructor
@Builder
public class EmailMessageBo {
	public final String to;
	public final String toFullname;
	public final String subject;
	public final String html;
	public final HashMap<String, ByteArrayResource> attachments;

	public EmailMessageBo(String to, String toFullname, String subject, String html) {
		this.to = to;
		this.toFullname = toFullname;
		this.subject = subject;
		this.html = html;
		this.attachments = new HashMap<>();
	}
}
