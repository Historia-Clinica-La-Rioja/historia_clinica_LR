package net.pladema.permissions.controller.dto;

import lombok.Getter;
import net.pladema.permissions.service.domain.LoggedUserBo;

@Getter
public class LoggedUserDto {

	private final Integer id;

	private final String email;

	private final LoggedPersonDto personDto;

	public LoggedUserDto(LoggedUserBo info) {
		this.email = info.getEmail();
		this.id = info.getId();
		this.personDto = new LoggedPersonDto(info.getFirstName(), info.getLastName());
	}
}
