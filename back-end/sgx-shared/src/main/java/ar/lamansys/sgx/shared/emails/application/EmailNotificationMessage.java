package ar.lamansys.sgx.shared.emails.application;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailNotificationMessage {
	public final String subject;
	public final String html;
}
