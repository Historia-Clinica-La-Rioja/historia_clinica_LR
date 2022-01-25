package net.pladema.permissions.service.domain;

public class LoggedUserBo {

	public final String email;
	public final Integer id;
	public final Integer personId;
	public final String firstName;
	public final String lastName;

	public LoggedUserBo(String email, Integer id, Integer personId, String firstName, String lastName) {
		this.email = email;
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
