package net.pladema.user.infrastructure.output;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.domain.PersonDataBo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.application.port.exceptions.UserPersonStorageEnumException;
import net.pladema.user.application.port.exceptions.UserPersonStorageException;
import net.pladema.user.application.port.exceptions.UserRoleStorageEnumException;
import net.pladema.user.application.port.exceptions.UserRoleStorageException;
import net.pladema.user.domain.UserRoleBo;
import net.pladema.user.repository.UserPersonRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleStorageImpl implements UserRoleStorage {

    private final UserRoleRepository userRoleRepository;

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    private final UserPersonRepository userPersonRepository;

	private final ProfessionalProfessionRepository professionalProfessionRepository;

    private final RoleService roleService;

	private final UserRepository userRepository;

    @Override
    public List<UserRoleBo> getRolesByUser(Integer userId) {
        List<UserRoleBo> result = new ArrayList<>();
        userRoleRepository.getRoleAssignments(userId)
                .forEach(r ->
                        result.add(new UserRoleBo(
                                r.getRole().getId(),
                                roleService.getRoleDescription(r.getRole()),
                                userId,
                                r.institutionId)));
        return result;
    }

    @Override
    public void updateUserRole(List<UserRoleBo> userRolesBo, Integer userId, Integer institutionId) {
        checkValidRoles(userRolesBo);
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId).stream()
                .filter(userRole -> userRole.getInstitutionId().equals(institutionId)).collect(Collectors.toList());
		List<Short> deletedUserRoles = userRoleRepository.findDeletedByUserId(userId).stream()
				.filter(userRole -> userRole.getInstitutionId().equals(institutionId))
				.map(UserRole::getRoleId)
				.collect(Collectors.toList());
        List<UserRole> newUserRoles = new ArrayList<>();
		List<UserRole> rolesToUpdate = new ArrayList<>();
        userRolesBo.forEach(userRoleBo -> {
			if (deletedUserRoles.contains(userRoleBo.getRoleId()))
				rolesToUpdate.add(new UserRole(userId, userRoleBo.getRoleId(), userRoleBo.getInstitutionId()));
			else
				newUserRoles.add(new UserRole(userId, userRoleBo.getRoleId(), userRoleBo.getInstitutionId()));
		});
        userRoleRepository.deleteAll(roleToDelete(userRoles , newUserRoles));
		rolesToUpdate.forEach(userRole ->
				userRoleRepository.setDeletedFalse(userRole.getUserId(), userRole.getRoleId(), userRole.getInstitutionId())
		);
        userRoleRepository.saveAll(roleToCreate(newUserRoles, userRoles));
    }

	@Override
	public boolean hasRoleInInstitution(Integer userId, Integer institutionId) {
		return userRoleRepository.hasRoleInInstitution(userId, institutionId);
	}

	@Override
	public List<PersonDataBo> getUsersByRoles(Integer institutionId, List<Short> rolesId) {
		log.debug("Input parameter -> institutionId {}, rolesId {}", institutionId, rolesId);
		return userRoleRepository.getUsersByRoles(institutionId, rolesId)
				.stream()
				.filter(p -> userRepository.userIsEnabled(p.getUserId()))
				.collect(Collectors.toList());
	}

	private List<UserRole> roleToCreate(List<UserRole> newRoles, List<UserRole> currentRoles) {
		return newRoles.stream()
				.filter(userRole ->
						currentRoles.stream().noneMatch(userRole1 -> areEquals(userRole, userRole1))
				)
				.collect(Collectors.toList());
	}
    private List<UserRole> roleToDelete(List<UserRole> currentRoles, List<UserRole> newRoles) {
        return currentRoles.stream()
                .filter(userRole ->
                        newRoles.stream().noneMatch(userRole1 -> areEquals(userRole, userRole1))
                )
                .collect(Collectors.toList());
    }

	private boolean areEquals(UserRole userRole, UserRole userRole1) {
		return userRole.getUserId().equals(userRole1.getUserId()) &&
				userRole.getRoleId().equals(userRole1.getRoleId()) &&
				userRole.getInstitutionId().equals(userRole1.getInstitutionId());
	}
    private void checkValidRoles(List<UserRoleBo> userRolesBo) {
        userRolesBo.stream().filter(role -> !isValidRole(role)).findAny()
                .ifPresent(userRoleDto -> {
                    throw new UserRoleStorageException(UserRoleStorageEnumException.PROFESSIONAL_REQUIRED,
                            String.format("El rol %s asignado requiere que el usuario sea un profesional", roleService.getRoleDescription(ERole.map(userRoleDto.getRoleId()))));
                });
    }

    private boolean isValidRole(UserRoleBo role) {
        return !isProfessional(role) || userPersonRepository.getPersonIdByUserId(role.getUserId())
                .map(personId -> healthcareProfessionalRepository.findProfessionalByPersonId(personId)
                .map(hp-> professionalProfessionRepository.countActiveByHealthcareProfessionalId(hp)>0)
                .orElse(false))
                .orElseThrow(() -> new UserPersonStorageException(UserPersonStorageEnumException.UNEXISTED_USER, "El usuario no existe"));
    }

    private boolean isProfessional(UserRoleBo role) {
        Short roleId = role.getRoleId();
        return ERole.ENFERMERO.getId().equals(roleId) ||
                ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
                ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
                ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId) ||
                ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId().equals(roleId) ||
				ERole.PRESCRIPTOR.getId().equals(roleId);
    }

}
