package net.pladema.sgx.session.infrastructure.input.rest.dto;


import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import net.pladema.authorization.domain.UserPersonaBo;

import javax.annotation.Nullable;

public class LoggedUserDto {

	public final Integer id;

	public final String email;

	public final LoggedPersonDto personDto;

	public final DateTimeDto previousLogin;

	@Nullable
	public final String cuil;

	public LoggedUserDto(UserPersonaBo info, String avatar, DateTimeDto previousLogin) {
		this.email = info.email;
		this.id = info.id;
		this.personDto = new LoggedPersonDto(info.firstName, info.lastName, avatar, info.nameSelfDetermination);
		this.previousLogin = previousLogin;
		this.cuil = info.cuil;
	}
}
