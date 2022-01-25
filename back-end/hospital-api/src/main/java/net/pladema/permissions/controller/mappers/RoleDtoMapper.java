package net.pladema.permissions.controller.mappers;

import net.pladema.permissions.controller.dto.RoleDto;
import net.pladema.permissions.service.domain.RoleBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface RoleDtoMapper {

    @Named("toListRoleDto")
    List<RoleDto> toListRoleDto(List<RoleBo> roleBoListo);

}
