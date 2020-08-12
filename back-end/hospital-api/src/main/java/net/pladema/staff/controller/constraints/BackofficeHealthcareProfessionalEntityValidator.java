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
        //nothing to do
    }

    @Override
    public void assertCreate(HealthcareProfessional entity) {
        if(healthcareProfessionalRepository.findProfessionalByPersonId(entity.getPersonId()).isPresent()){
            throw new BackofficeValidationException("healthcareprofessional.exists");
        }
    }

    @Override
    public void assertDelete(Integer id){
        Optional<HealthcareProfessional> healthcareProfessional = healthcareProfessionalRepository.findById(id);
        if(healthcareProfessional.isPresent()) {
            Optional<Integer> userId = userRepository.getUserIdByPersonId(healthcareProfessional.get().getPersonId());
            List<RoleAssignment> rolesList = userRoleRepository.getRoleAssignments(userId.get());
            if (userId.isPresent() && !rolesList.isEmpty()) {
                if(rolesList.stream().anyMatch(ra -> isProfessionalRole(ra)))
                     throw new BackofficeValidationException("user.hasrole");
            }
        }
    }

    private boolean isProfessionalRole(RoleAssignment role) {
        Short roleId = role.getRole().getId();
        return ERole.ENFERMERO.getId().equals(roleId) ||
                ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
                ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
                ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId);
    }
}
