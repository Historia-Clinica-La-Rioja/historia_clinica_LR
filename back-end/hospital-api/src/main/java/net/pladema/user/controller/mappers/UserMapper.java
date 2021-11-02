package net.pladema.user.controller.mappers;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import net.pladema.user.controller.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

	@Mapping(target = "email", source = "username")
	UserDto fromUser(User createdUser);
}
