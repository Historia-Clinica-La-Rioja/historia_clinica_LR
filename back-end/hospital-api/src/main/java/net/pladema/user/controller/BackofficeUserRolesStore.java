package net.pladema.user.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import ar.lamansys.sgx.shared.admin.AdminConfiguration;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.repository.enums.ERoleLevel;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.user.controller.exceptions.BackofficeUserException;
import net.pladema.user.controller.exceptions.BackofficeUserExceptionEnum;

@Service
public class BackofficeUserRolesStore implements BackofficeStore<UserRole, Long> {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;
	private final ProfessionalProfessionRepository professionalProfessionRepository;

	public BackofficeUserRolesStore(UserRepository userRepository,
									UserRoleRepository userRoleRepository,
									HealthcareProfessionalRepository healthcareProfessionalRepository,
									ProfessionalProfessionRepository professionalProfessionRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
		this.professionalProfessionRepository = professionalProfessionRepository;
	}

	@Override
	public Page<UserRole> findAll(UserRole entity, Pageable pageable) {
		List<UserRole> content = toList(userRoleRepository.findAll())
				.stream()
				.filter(userRole -> this.filterByExample(userRole, entity))
				.filter(this::filterRoles)
				.collect(Collectors.toList());
		return new PageImpl<>(content, pageable, content.size());
	}

	private boolean filterByExample(UserRole userRole, UserRole entity) {
		if (entity.getUserId() != null && !entity.getUserId().equals(userRole.getUserId()))
			return false;
		if (entity.getRoleId() != null && !entity.getRoleId().equals(userRole.getRoleId()))
			return false;
		return !userRole.isDeleted();
	}

	private boolean filterRoles(UserRole userRole) {
		return !ERole.ROOT.getId().equals(userRole.getRoleId()) &&
				!ERole.API_CONSUMER.getId().equals(userRole.getRoleId()) &&
				!ERole.PARTIALLY_AUTHENTICATED.getId().equals(userRole.getRoleId());
	}

	@Override
	public List<UserRole> findAll() {
		return toList(userRoleRepository.findAll());
	}

	@Override
	public List<UserRole> findAllById(List<Long> ids) {
		return toList(userRoleRepository.findAllById(ids));
	}

	@Override
	public Optional<UserRole> findById(Long id) {
		return userRoleRepository.findById(id);
	}

	@Override
	public UserRole save(UserRole entity) {
		validate(entity);
		if (entity.getInstitutionId() == null)
			entity.setInstitutionId(-1);
		return userRoleRepository
				.findByRoleInstitutionAndUserId(entity.getUserId(), entity.getRoleId(), entity.getInstitutionId())
				.map(userRoleRepository::reactivate)
				.orElseGet( () -> userRoleRepository.save(entity));
	}

	private void validate(UserRole userRole) {
		userRepository.findById(userRole.getUserId())
				.map(user -> checkValidRole(userRole, user))
				.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER,
						String.format("El usuario %s no existe", userRole.getUserId())));
	}

	private User checkValidRole(UserRole userRole, User user) {
		checkProfessionalRole(userRole, user);
		if(isROOTUser(user.getUsername()) &&
				!userRoleRepository.findByRoleInstitutionAndUserId(userRole.getUserId(), ERole.ROOT.getId(), -1).isPresent())
			throw new BackofficeUserException(BackofficeUserExceptionEnum.ROOT_LOST_PERMISSION, "El admin no puede perder el rol: ROOT");
		if(!isROOTUser(user.getUsername()) && isRootRole(userRole))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El usuario creado no puede tener el siguiente rol: ROOT");
		if (ERoleLevel.LEVEL1.equals(ERole.map(userRole.getRoleId()).getLevel()) && !assignedInstitution(userRole))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El tipo de rol requiere definir una institución ");
		if (ERoleLevel.LEVEL1.equals(ERole.map(userRole.getRoleId()).getLevel()) && !assignedInstitution(userRole))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El tipo de rol requiere definir una institución ");
		if (ERoleLevel.LEVEL0.equals(ERole.map(userRole.getRoleId()).getLevel()) && assignedInstitution(userRole))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El tipo de rol no debe asociarse a una institución ");
		return user;
	}

	private boolean assignedInstitution(UserRole userRole) {
		return userRole.getInstitutionId() != null  && !userRole.getInstitutionId().equals(-1);
	}

	private boolean isROOTUser(String username) {
		return AdminConfiguration.ADMIN_USERNAME_DEFAULT.equals(username);
	}
	private boolean isRootRole(UserRole role) {
		Short roleId = role.getRoleId();
		return ERole.ROOT.getId().equals(roleId);
	}
	private void checkProfessionalRole(UserRole userRole, User user) {
		if(!isProfessional(userRole))
			return;
		var professionalId = healthcareProfessionalRepository.getProfessionalId(userRole.getUserId());
		if ((professionalId == null) || (professionalProfessionRepository.countActiveByHealthcareProfessionalId(professionalId)<=0))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.PROFESSIONAL_REQUIRED,
					String.format("El rol %s asignado requiere que el usuario sea un profesional", userRole.getRoleId()));
	}

	private boolean isProfessional(UserRole role) {
		Short roleId = role.getRoleId();
		return ERole.ENFERMERO.getId().equals(roleId) ||
				ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
				ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
				ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId) ||
				ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId().equals(roleId);
	}

	@Override
	public void deleteById(Long id) {

		var userRole = userRoleRepository.findById(id)
				.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER,
						String.format("El usuario no contiene un rol con id %s", id)));

		if (wantDeleteRootPermission(userRole.getRoleId())) {
			var user = userRepository.findById(userRole.getUserId())
					.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER,
							String.format("El usuario no contiene un rol con id %s", id)));
			if(isROOTUser(user.getUsername()))
				throw new BackofficeUserException(BackofficeUserExceptionEnum.ROOT_LOST_PERMISSION, "El admin no puede perder el rol: ROOT");
		}
		userRoleRepository.deleteById(id);
	}

	private boolean wantDeleteRootPermission(Short roleId) {
		return ERole.ROOT.getId().equals(roleId);
	}

	@Override
	public Example<UserRole> buildExample(UserRole entity) {
		return Example.of(entity);
	}

	private static List<UserRole> toList(Iterable<UserRole> roles) {
		return StreamSupport
				.stream(roles.spliterator(), false)
				.collect(Collectors.toList());
	}

}
