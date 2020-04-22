package net.pladema.user.controller.mappers;

import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

	@Mapping(target = "email", source = "username")
	UserDto fromUser(User createdUser);
}
