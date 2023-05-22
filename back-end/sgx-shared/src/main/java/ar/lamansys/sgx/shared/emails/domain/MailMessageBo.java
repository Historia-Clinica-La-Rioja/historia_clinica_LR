package ar.lamansys.sgx.shared.emails.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class MailMessageBo {
	public final String subject;
	public final String body;
	public final List<StoredFileBo> attachments;

	public MailMessageBo(String subject, String body) {
		this.subject = subject;
		this.body = body;
		attachments = Collections.emptyList();
	}
}
