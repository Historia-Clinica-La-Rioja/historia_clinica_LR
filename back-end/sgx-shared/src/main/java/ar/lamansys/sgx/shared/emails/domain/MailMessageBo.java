package ar.lamansys.sgx.shared.emails.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class MailMessageBo {
	public final String subject;
	public final String body;
}
