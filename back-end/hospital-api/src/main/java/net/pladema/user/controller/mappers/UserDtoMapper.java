package net.pladema.user.controller.mappers;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.repository.entity.VHospitalUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface UserDtoMapper {

	@Named("userToDto")
	BackofficeUserDto toDto(User user);

	@Named("userInfoToDto")
	@Mapping(target = "enable", source = "user.enabled")
	BackofficeUserDto toDto(UserInfoDto user);

	@Named("dtoToUser")
	User toModel(BackofficeUserDto dto);

	@Named("fromVHospitalUserToDto")
	@Mapping(target = "id", source = "user.userId")
	BackofficeUserDto fromVHospitalUserToDto(VHospitalUser user);

	@Named("toVHospitalUser")
	@Mapping(target = "userId", source = "dto.id")
	VHospitalUser toVHospitalUser(BackofficeUserDto dto);

	@Named("userWithDto")
	@Mapping(target = "lastLogin", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", source = "dto.username")
	@Mapping(target = "enable", source = "dto.enable")
	User toModel(BackofficeUserDto dto, @MappingTarget User model);
}
