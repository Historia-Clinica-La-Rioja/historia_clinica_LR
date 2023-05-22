package net.pladema.person.infraestructure.output.notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonRecipient {
	public final Integer personId;
	public final String email;

	public PersonRecipient(Integer personId){
		this.personId = personId;
		this.email = null;
	}
}
