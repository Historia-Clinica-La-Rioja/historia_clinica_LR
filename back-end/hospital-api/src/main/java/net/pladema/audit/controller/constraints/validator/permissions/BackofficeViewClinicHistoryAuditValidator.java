


package net.pladema.audit.controller.constraints.validator.permissions;

import lombok.AllArgsConstructor;
import net.pladema.audit.repository.entity.ViewClinicHistoryAudit;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class BackofficeViewClinicHistoryAuditValidator implements BackofficePermissionValidator<ViewClinicHistoryAudit, Integer> {

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	@Override
	public void assertGetList(ViewClinicHistoryAudit entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('AUDITORIA_DE_ACCESO')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('AUDITORIA_DE_ACCESO')")
	public void assertGetOne(Integer id) {

	}

	@Override
	public void assertCreate(ViewClinicHistoryAudit entity) {

	}

	@Override
	public void assertUpdate(Integer id, ViewClinicHistoryAudit entity) {

	}

	@Override
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('AUDITORIA_DE_ACCESO')")
	public ItemsAllowed<Integer> itemsAllowedToList(ViewClinicHistoryAudit entity) {
		if (authoritiesValidator.hasRole(ERole.AUDITORIA_DE_ACCESO))
			return new ItemsAllowed<>();
		return null;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('AUDITORIA_DE_ACCESO')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.AUDITORIA_DE_ACCESO))
			return new ItemsAllowed<>();
		return null;
	}
}
