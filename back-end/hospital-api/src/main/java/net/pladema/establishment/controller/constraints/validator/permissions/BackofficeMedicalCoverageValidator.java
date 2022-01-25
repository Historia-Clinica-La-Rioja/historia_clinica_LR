package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.patient.controller.dto.BackofficeCoverageDto;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeMedicalCoverageValidator implements BackofficePermissionValidator<BackofficeCoverageDto, Integer> {

    public BackofficeMedicalCoverageValidator() {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetList(BackofficeCoverageDto entity) {
    }

    @Override
    public List<Integer> filterIdsByPermission(List<Integer> ids) {
        return ids;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetOne(Integer id) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertCreate(BackofficeCoverageDto entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, BackofficeCoverageDto entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertDelete(Integer id) {
    }

    @Override
    public ItemsAllowed itemsAllowedToList(BackofficeCoverageDto entity) {
        return new ItemsAllowed<>();
    }

    @Override
    public ItemsAllowed itemsAllowedToList() {
        return new ItemsAllowed<>();
    }

}
