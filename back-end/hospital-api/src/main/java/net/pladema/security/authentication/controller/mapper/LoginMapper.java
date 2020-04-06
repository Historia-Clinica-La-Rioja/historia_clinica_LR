package net.pladema.security.authentication.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.security.authentication.controller.dto.LoginDto;
import net.pladema.security.token.service.domain.Login;

@Mapper
public interface LoginMapper {

	@Named("mapLogin")
	Login mapLogin(LoginDto source);

}
