package net.pladema.sgx.loggeduser.infrastructure.input.rest.dto;

import net.pladema.permissions.service.domain.LoggedUserBo;

public class LoggedUserDto {

	public final Integer id;

	public final String email;

	public final LoggedPersonDto personDto;

	public LoggedUserDto(LoggedUserBo info) {
		this.email = info.email;
		this.id = info.id;
		this.personDto = new LoggedPersonDto(info.firstName, info.lastName);
	}
}
