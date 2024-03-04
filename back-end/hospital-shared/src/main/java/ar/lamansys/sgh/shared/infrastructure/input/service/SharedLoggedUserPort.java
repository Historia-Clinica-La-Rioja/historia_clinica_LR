package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedLoggedUserPort {

	boolean hasAdministrativeRole(Integer institutionId);

	boolean hasLocalManagerRoleOrRegionalManagerRole(Integer userId);

	 boolean hasDomainManagerRole(Integer userId);

	boolean hasManagerRole(Integer userId);

	List<RoleInfoDto> getRoles(Integer userId);

	List<Short> getLoggedUserRoleIds(Integer institutionId, Integer userId);

}
