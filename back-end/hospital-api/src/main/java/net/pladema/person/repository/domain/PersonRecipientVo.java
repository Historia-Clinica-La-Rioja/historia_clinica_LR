package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonRecipientVo {
	public final String firstName;
	public final String lastName;
	public final String nameSelfDetermination;
	public final String phonePrefix;
	public final String phoneNumber;
	public final String email;
}
