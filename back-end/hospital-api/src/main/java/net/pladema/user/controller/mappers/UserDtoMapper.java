package net.pladema.user.controller.mappers;

import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface UserDtoMapper {

	@Named("userToDto")
	BackofficeUserDto toDto(User user);

	@Named("dtoToUser")
	User toModel(BackofficeUserDto dto);

	@Named("userWithDto")
	@Mapping(target = "personId", ignore = true)
	@Mapping(target = "lastLogin", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", source = "dto.username")
	@Mapping(target = "enable", source = "dto.enable")
	User toModel(BackofficeUserDto dto, @MappingTarget User model);
}
