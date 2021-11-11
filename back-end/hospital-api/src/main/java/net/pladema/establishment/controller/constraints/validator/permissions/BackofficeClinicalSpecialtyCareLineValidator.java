package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.ClinicalSpecialtyCareLineRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtyCareLine;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeClinicalSpecialtyCareLineValidator implements BackofficePermissionValidator<ClinicalSpecialtyCareLine, Integer> {

    private final ClinicalSpecialtyCareLineRepository repository;

    public BackofficeClinicalSpecialtyCareLineValidator(ClinicalSpecialtyCareLineRepository repository) {
        this.repository = repository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetList(ClinicalSpecialtyCareLine entity) {
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
    public void assertCreate(ClinicalSpecialtyCareLine entity) {
        ClinicalSpecialtyCareLine clinicalSpecialtyCareLine = this.repository.findByCareLineIdAndClinicalSpecialtyId(entity.getCareLineId(), entity.getClinicalSpecialtyId());
        if (clinicalSpecialtyCareLine != null && !clinicalSpecialtyCareLine.isDeleted())
            throw new BackofficeValidationException("care-line.clinical-specialty-exists");
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, ClinicalSpecialtyCareLine entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertDelete(Integer id) {
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList(ClinicalSpecialtyCareLine entity) {
        return new ItemsAllowed<>();
    }

    @Override
    public ItemsAllowed<Integer> itemsAllowedToList() {
        return null;
    }

}
