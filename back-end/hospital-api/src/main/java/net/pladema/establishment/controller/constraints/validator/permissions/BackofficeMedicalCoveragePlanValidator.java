package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeMedicalCoveragePlanValidator implements BackofficePermissionValidator<MedicalCoveragePlan, Integer> {

    private final MedicalCoveragePlanRepository repository;

    public BackofficeMedicalCoveragePlanValidator(MedicalCoveragePlanRepository repository) {
        this.repository = repository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetList(MedicalCoveragePlan entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public List<Integer> filterIdsByPermission(List<Integer> ids) {
        return ids;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetOne(Integer id) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertCreate(MedicalCoveragePlan entity) {
		this.repository.findByIdAndPlan(entity.getMedicalCoverageId(), entity.getPlan().toLowerCase()).ifPresent(p -> {
			if (!p.isDeleted()) throw new BackofficeValidationException("medical-coverage.plan-exists");
		});
	}

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, MedicalCoveragePlan entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertDelete(Integer id) {
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList(MedicalCoveragePlan entity) {
        return new ItemsAllowed<>();
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList() {
        return null;
    }

}
