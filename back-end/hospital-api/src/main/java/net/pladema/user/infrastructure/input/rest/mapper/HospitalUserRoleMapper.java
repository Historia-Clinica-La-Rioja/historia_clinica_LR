package net.pladema.user.infrastructure.input.rest.mapper;

import net.pladema.user.domain.UserRoleBo;
import net.pladema.user.infrastructure.input.rest.dto.UserRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface HospitalUserRoleMapper {

    @Named("toListUserRoleDto")
    List<UserRoleDto> toListUserRoleDto(List<UserRoleBo> userRoles);

    @Named("toListUserRoleBo")
    List<UserRoleBo> toListUserRoleBo(List<UserRoleDto> userRoles);

}
