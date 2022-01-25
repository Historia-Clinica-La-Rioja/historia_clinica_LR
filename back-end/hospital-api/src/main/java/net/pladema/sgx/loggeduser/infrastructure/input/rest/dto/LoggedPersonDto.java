package net.pladema.sgx.loggeduser.infrastructure.input.rest.dto;

public class LoggedPersonDto {

	public final String firstName;

	public final String lastName;

	public LoggedPersonDto(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
