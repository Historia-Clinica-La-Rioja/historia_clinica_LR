package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.PrivateHealthInsurancePlanRepository;
import net.pladema.establishment.repository.entity.PrivateHealthInsurancePlan;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficePrivateHealthInsurancePlanValidator implements BackofficePermissionValidator<PrivateHealthInsurancePlan, Integer> {

    private final PrivateHealthInsurancePlanRepository repository;

    public BackofficePrivateHealthInsurancePlanValidator(PrivateHealthInsurancePlanRepository repository) {
        this.repository = repository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetList(PrivateHealthInsurancePlan entity) {
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
    public void assertCreate(PrivateHealthInsurancePlan entity) {
        PrivateHealthInsurancePlan privateHealthInsurancePlan = this.repository.findByIdAndPlan(entity.getPrivateHealthInsuranceId(), entity.getPlan());
        if (privateHealthInsurancePlan != null)
            throw new BackofficeValidationException("medical-coverage.plan-exists");
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, PrivateHealthInsurancePlan entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertDelete(Integer id) {
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList(PrivateHealthInsurancePlan entity) {
        return new ItemsAllowed<>();
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList() {
        return null;
    }

}
