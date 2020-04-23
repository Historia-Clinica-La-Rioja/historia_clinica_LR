package net.pladema.permissions.controller.mappers;

import org.mapstruct.Mapper;

import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.repository.entity.UserRole;

@Mapper
public interface UserRoleDtoMapper {
	
	BackofficeUserRoleDto toDto(UserRole userRole);
	
	UserRole toModel(BackofficeUserRoleDto userRoleDto);
}
