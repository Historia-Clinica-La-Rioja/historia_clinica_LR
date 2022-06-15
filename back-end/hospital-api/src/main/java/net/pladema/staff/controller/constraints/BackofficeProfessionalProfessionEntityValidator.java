package net.pladema.staff.controller.constraints;

import java.util.List;

import org.springframework.stereotype.Component;

import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.staff.controller.dto.ProfessionalProfessionBackofficeDto;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.staff.repository.entity.ProfessionalProfessions;
import net.pladema.user.repository.UserPersonRepository;

@Component
public class BackofficeProfessionalProfessionEntityValidator extends BackofficeEntityValidatorAdapter<ProfessionalProfessionBackofficeDto, Integer> {

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserPersonRepository userPersonRepository;

	private final ProfessionalProfessionRepository professionalProfessionRepository;

    public BackofficeProfessionalProfessionEntityValidator(
			HealthcareProfessionalRepository healthcareProfessionalRepository,
			UserPersonRepository userPersonRepository,
			UserRoleRepository userRoleRepository, ProfessionalProfessionRepository professionalProfessionRepository) {
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPersonRepository = userPersonRepository;
		this.professionalProfessionRepository = professionalProfessionRepository;
	}

    @Override
    public void assertUpdate(Integer id, ProfessionalProfessionBackofficeDto entity) {
		ProfessionalProfessions professionalProfessions = getProfessionalProfessions(id);
		if(!professionalProfessions.getHealthcareProfessionalId().equals(entity.getHealthcareProfessionalId())) {
			assertCreate(entity);
			assertDelete(id);
		}
    }

    @Override
    public void assertCreate(ProfessionalProfessionBackofficeDto entity) {
		if (entity.getClinicalSpecialtyId() == null)
			throw new BackofficeValidationException("healthcareprofessional.linkprofession");
    }

    @Override
    public void assertDelete(Integer id){
		ProfessionalProfessions professionalProfessions = getProfessionalProfessions(id);
		int count = professionalProfessionRepository.countActiveByHealthcareProfessionalId(professionalProfessions.getHealthcareProfessionalId());
        if (count < 2) {
			healthcareProfessionalRepository.findById(professionalProfessions.getHealthcareProfessionalId())
					.flatMap(hp -> userPersonRepository.getUserIdByPersonId(hp.getPersonId()))
					.ifPresent(this::checkRoles);
		}
    }

    private void checkRoles(Integer userId){
        List<RoleAssignment> rolesList = userRoleRepository.getRoleAssignments(userId);
        if (rolesList.stream().anyMatch(this::isProfessionalRole)){
            throw new BackofficeValidationException("user.hasrole");
        }
    }

    private ProfessionalProfessions getProfessionalProfessions(Integer id){
        return professionalProfessionRepository.findById(id).orElseThrow(
                () -> new BackofficeValidationException("healthcareprofessional.exists")
        );
    }

    private boolean isProfessionalRole(RoleAssignment role) {
        Short roleId = role.getRole().getId();
        return ERole.ENFERMERO.getId().equals(roleId) ||
                ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
                ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
                ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId) ||
                ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId().equals(roleId);
    }
}
