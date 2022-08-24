package ar.lamansys.sgx.shared.emails.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class EmailMessageBo {
	public final String to;
	public final String toFullname;
	public final String subject;
	public final String html;
}
