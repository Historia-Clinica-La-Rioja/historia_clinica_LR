package net.pladema.user.controller.mappers;

import net.pladema.user.controller.dto.AbstractUserDto;
import net.pladema.user.controller.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import net.pladema.user.controller.dto.AddUserDto;
import net.pladema.user.repository.entity.User;

@Mapper
public interface UserMapper {

	
	@Named("mapNewUser")
	@Mapping(target = "username", source = "email")
	User mapNewUser(AddUserDto source);

	@Mapping(target = "email", source = "username")
	UserDto fromUser(User createdUser);
}
