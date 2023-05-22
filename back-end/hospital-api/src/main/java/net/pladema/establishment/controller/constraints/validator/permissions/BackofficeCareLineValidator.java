package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.entity.CareLine;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeCareLineValidator implements BackofficePermissionValidator<CareLine, Integer> {

    public BackofficeCareLineValidator() {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
    public void assertGetList(CareLine entity) {
    }

    @Override
    public List<Integer> filterIdsByPermission(List<Integer> ids) {
        return ids;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
    public void assertGetOne(Integer id) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertCreate(CareLine entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, CareLine entity) {
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertDelete(Integer id) {
    }

    @Override
    public ItemsAllowed itemsAllowedToList(CareLine entity) {
        return new ItemsAllowed<>();
    }

    @Override
    public ItemsAllowed itemsAllowedToList() {
        return new ItemsAllowed<>();
    }

}
