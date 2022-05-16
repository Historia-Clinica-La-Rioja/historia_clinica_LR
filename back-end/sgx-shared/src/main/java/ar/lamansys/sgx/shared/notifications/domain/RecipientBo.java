package ar.lamansys.sgx.shared.notifications.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class RecipientBo {
	public final String firstname;
	public final String lastname;
	public final String email;
	public final String phoneNumber;
}
