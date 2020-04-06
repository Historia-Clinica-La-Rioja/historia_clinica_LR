package net.pladema.security.authentication.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.security.authentication.controller.dto.JWTokenDto;
import net.pladema.security.token.service.domain.JWToken;

@Mapper
public interface JWTokenMapper {

	@Named("mapNewToken")
	JWTokenDto mapNewToken(JWToken source);
	
}
