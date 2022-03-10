package net.pladema.authorization.domain;

import java.util.function.Supplier;

public class UserPersonaBo {

	public final String email;
	public final Integer id;
	public final Integer personId;
	public final String firstName;
	public final String lastName;
	public final Supplier<String> avatar;

	public UserPersonaBo(String email, Integer id, Integer personId, String firstName, String lastName, Supplier<String> avatar) {
		this.email = email;
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
	}

}
