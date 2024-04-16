package net.pladema.user.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.pladema.establishment.repository.InstitutionalGroupUserRepository;
import net.pladema.permissions.repository.RoleRepository;

import net.pladema.permissions.repository.entity.Role;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import ar.lamansys.sgx.shared.admin.AdminConfiguration;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.repository.enums.ERoleLevel;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.user.controller.exceptions.BackofficeUserException;
import net.pladema.user.controller.exceptions.BackofficeUserExceptionEnum;
import net.pladema.user.controller.filters.BackofficeRolesFilter;

@AllArgsConstructor
@Service
public class BackofficeUserRolesStore implements BackofficeStore<UserRole, Long> {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;
	private final ProfessionalProfessionRepository professionalProfessionRepository;
	private final BackofficeRolesFilter backofficeRolesFilter;
	private final RoleRepository roleRepository;
	private final InstitutionalGroupUserRepository institutionalGroupUserRepository;


	@Override
	public Page<UserRole> findAll(UserRole entity, Pageable pageable) {
		List<UserRole> content = toList(userRoleRepository.findAll())
				.stream()
				.filter(userRole -> this.filterByExample(userRole, entity))
				.filter(backofficeRolesFilter::filterRoles)
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

		try {
			Optional<UserRole> userRoleAlreadyExist = userRoleRepository.findByRoleInstitutionAndUserId(
					entity.getUserId(),
					entity.getRoleId(),
					entity.getInstitutionId()
			);
			if (userRoleAlreadyExist.isPresent())
				throw new DataIntegrityViolationException("El usuario ya cuenta con ese rol en la institución");
			return userRoleRepository
					.getUserRoleIfIsDeleted(entity.getUserId(), entity.getRoleId(), entity.getInstitutionId())
					.map(userRoleRepository::reactivate)
					.orElseGet( () -> userRoleRepository.save(entity));
		} catch (DataIntegrityViolationException e) {
			throw new BackofficeUserException(BackofficeUserExceptionEnum.ROLE_ALREADY_ASSIGNED, "El usuario ya cuenta con ese rol en la institución");
		}
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
		if (ERoleLevel.LEVEL0.equals(ERole.map(userRole.getRoleId()).getLevel()) && assignedInstitution(userRole))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El tipo de rol no debe asociarse a una institución ");
		checkManagerRole(userRole);
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
		if ((professionalId == null) || (professionalProfessionRepository.countActiveByHealthcareProfessionalId(professionalId)<=0)) {
			String roleDescription = roleRepository.findById(userRole.getRoleId()).map(Role::getDescription).orElse("ERROR");
			throw new BackofficeUserException(BackofficeUserExceptionEnum.PROFESSIONAL_REQUIRED,
					String.format("El rol %s asignado requiere que el usuario sea un profesional", roleDescription));
		}
	}

	private boolean isProfessional(UserRole role) {
		Short roleId = role.getRoleId();
		return ERole.ENFERMERO.getId().equals(roleId) ||
				ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
				ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
				ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId) ||
				ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId().equals(roleId) ||
				ERole.VIRTUAL_CONSULTATION_PROFESSIONAL.getId().equals(roleId) ||
				ERole.VIRTUAL_CONSULTATION_RESPONSIBLE.getId().equals(roleId) ||
				ERole.ABORDAJE_VIOLENCIAS.getId().equals(roleId);
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
		if (isManager(userRole) && institutionalGroupUserRepository.existsByUserId(userRole.getUserId()))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_BELONGS_TO_GROUP, "El usuario no puede perder el rol ya que se encuentra asociado a grupos institucionales");
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

	private void checkManagerRole(UserRole userRole){
		if (!isManager(userRole))
			return;
		List<Short> rolesIds = userRoleRepository.findByUserId(userRole.getUserId()).stream().map(UserRole::getRoleId).collect(Collectors.toList());
		if (rolesIds.contains(ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId()) || rolesIds.contains(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) || rolesIds.contains(ERole.GESTOR_DE_ACCESO_LOCAL.getId()))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El usuario ya cuenta con un rol de tipo Gestor");
	}

	private boolean isManager(UserRole role){
		Short roleId = role.getRoleId();
		return roleId.equals(ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId()) ||
				roleId.equals(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) ||
				roleId.equals(ERole.GESTOR_DE_ACCESO_LOCAL.getId());
	}

}
