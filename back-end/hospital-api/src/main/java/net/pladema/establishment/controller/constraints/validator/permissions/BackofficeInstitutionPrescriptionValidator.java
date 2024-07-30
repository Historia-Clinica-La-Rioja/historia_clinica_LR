package net.pladema.establishment.controller.constraints.validator.permissions;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.InstitutionPrescription;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.application.port.UserSessionStorage;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

@Component
public class BackofficeInstitutionPrescriptionValidator implements BackofficePermissionValidator<InstitutionPrescription, Integer> {

	public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";
	private final InstitutionRepository repository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	private final Supplier<Boolean> userCanView;

	public BackofficeInstitutionPrescriptionValidator(
			InstitutionRepository repository,
			BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
			UserSessionStorage userSessionStorage,
			PermissionEvaluator permissionEvaluator
	) {
		this.repository = repository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
		this.userCanView = userSessionStorage.hasAnyRole(
				// quien puede ver las instituciones
				ERole.ROOT,
				ERole.ADMINISTRADOR
		);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(InstitutionPrescription entity) {
		// nothing to do
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (userCanView.get())
			return ids;
		return Collections.emptyList();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetOne(Integer id) {
		// Do nothing
	}

	@Override
	public void assertCreate(InstitutionPrescription entity) {
		throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, InstitutionPrescription entity) {
		throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertDelete(Integer id) {
		throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}

	@Override
	public ItemsAllowed itemsAllowedToList(InstitutionPrescription entity) {
		if (userCanView.get())
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		if (userCanView.get())
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

}
