package net.pladema.staff.controller.constraints;

import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BackofficeHealthcareProfessionalEntityValidator extends BackofficeEntityValidatorAdapter<HealthcareProfessional, Integer> {

    HealthcareProfessionalRepository healthcareProfessionalRepository;
    UserRoleRepository userRoleRepository;
    UserRepository userRepository;

    public BackofficeHealthcareProfessionalEntityValidator(
            HealthcareProfessionalRepository healthcareProfessionalRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository) {
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void assertUpdate(Integer id, HealthcareProfessional entity) {
            HealthcareProfessional hp = getHealthcareProfessional(id);
            if(!hp.getPersonId().equals(entity.getPersonId())) {
                assertCreate(entity);
                assertDelete(id);
            }
    }

    @Override
    public void assertCreate(HealthcareProfessional entity) {
        if(healthcareProfessionalRepository.findProfessionalByPersonId(entity.getPersonId()).isPresent()){
            throw new BackofficeValidationException("healthcareprofessional.exists");
        }
    }

    @Override
    public void assertDelete(Integer id){
        HealthcareProfessional healthcareProfessional = getHealthcareProfessional(id);
        userRepository.getUserIdByPersonId(healthcareProfessional
                .getPersonId())
                .ifPresent(this::checkRoles);
    }

    private void checkRoles(Integer userId){
        List<RoleAssignment> rolesList = userRoleRepository.getRoleAssignments(userId);
        if (rolesList.stream().anyMatch(this::isProfessionalRole)){
            throw new BackofficeValidationException("user.hasrole");
        }
    }

    private HealthcareProfessional getHealthcareProfessional(Integer id){
        return healthcareProfessionalRepository.findById(id).orElseThrow(
                () -> new BackofficeValidationException("healthcareprofessional.exists")
        );
    }

    private boolean isProfessionalRole(RoleAssignment role) {
        Short roleId = role.getRole().getId();
        return ERole.ENFERMERO.getId().equals(roleId) ||
                ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
                ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
                ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId);
    }
}
