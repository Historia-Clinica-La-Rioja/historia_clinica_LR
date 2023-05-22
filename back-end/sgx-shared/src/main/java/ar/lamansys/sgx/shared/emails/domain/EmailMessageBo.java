package ar.lamansys.sgx.shared.emails.domain;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.core.io.ByteArrayResource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Builder
public class EmailMessageBo {
	public final String to;
	public final String toFullname;
	public final String subject;
	public final String html;
	public final List<StoredFileBo> attachments;

	public EmailMessageBo(String to, String toFullname, String subject, String html) {
		this.to = to;
		this.toFullname = toFullname;
		this.subject = subject;
		this.html = html;
		this.attachments = Collections.emptyList();
	}
}
