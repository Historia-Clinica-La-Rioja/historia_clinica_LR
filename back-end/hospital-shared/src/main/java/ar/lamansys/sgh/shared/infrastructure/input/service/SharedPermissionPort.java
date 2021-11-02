package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedPermissionPort {

    List<RoleInfoDto> ferPermissionInfoByUserId(Integer userId);
}
