package net.pladema.medicalconsultation.diary.controller.permissions;

import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;
import org.springframework.data.domain.Example;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeDoctorsOfficeValidator implements BackofficePermissionValidator<DoctorsOffice, Integer> {

    public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";
    private final DoctorsOfficeRepository repository;

    private final BackofficeAuthoritiesValidator authoritiesValidator;

    private final PermissionEvaluator permissionEvaluator;


    public BackofficeDoctorsOfficeValidator(DoctorsOfficeRepository repository,
                                            BackofficeAuthoritiesValidator authoritiesValidator,
                                            PermissionEvaluator permissionEvaluator) {
        this.repository = repository;
        this.authoritiesValidator = authoritiesValidator;
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    @PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertGetList(DoctorsOffice entity) {
        //nothing to do
    }

    @Override
    public List<Integer> filterIdsByPermission(List<Integer> ids) {

        if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
            return ids;
        return ids.stream().filter(id -> {
            try {
                Integer institutionId = repository.getInstitutionId(id);
                hasPermissionByInstitution(institutionId);
                return true;
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void assertGetOne(Integer id) {
        if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
            return;
        Integer institutionId = repository.getInstitutionId(id);
        hasPermissionByInstitution(institutionId);
    }

    @Override
    @PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertCreate(DoctorsOffice entity) {
        //nothing to do
    }

    @Override
    @PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
    public void assertUpdate(Integer id, DoctorsOffice entity) {
        //nothing to do
    }

    @Override
    public void assertDelete(Integer id) {
        if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
            return;
        Integer institutionId = repository.getInstitutionId(id);
        hasPermissionByInstitution(institutionId);
    }

    @Override
    public ItemsAllowed itemsAllowedToList(DoctorsOffice entity) {
        if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
            return new ItemsAllowed<>();

        List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
        if (allowedInstitutions.isEmpty())
            return new ItemsAllowed<>(false, Collections.emptyList());

        List<DoctorsOffice> entitiesByExample = repository.findAll(Example.of(entity));
        List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
        List<Integer> resultIds = entitiesByExample.stream().filter(css -> idsAllowed.contains(css.getId())).map(DoctorsOffice::getId).collect(Collectors.toList());
        return new ItemsAllowed<>(false, resultIds);
    }

    @Override
    public ItemsAllowed itemsAllowedToList() {
        if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
            return new ItemsAllowed<>();
        List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
        if (allowedInstitutions.isEmpty())
            return new ItemsAllowed<>(false, Collections.emptyList());
        List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
        return new ItemsAllowed<>(false, idsAllowed);
    }

    private void hasPermissionByInstitution(Integer institutionId) {
        if (institutionId == null)
            throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
            throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
        if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
            throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
    }
}
