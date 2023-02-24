package net.pladema.sgh.app.seeds.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.address.repository.AddressRepository;
import net.pladema.address.repository.entity.Address;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.entity.UserPerson;

@Slf4j
@AllArgsConstructor
@Service
public class SampleData {

	private final SampleProperties sampleProperties;
	private final AddressRepository addressRepository;
	private final InstitutionRepository institutionRepository;
	private final UserExternalService userExternalService;
	private final UserAssignmentService userAssignmentService;
	private final PersonRepository personRepository;
	private final UserPersonRepository userPersonRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	private final Environment environment;

	public boolean shouldRun() {
		// debería correr solo en ambientes específicos
		return isDevProfileActivated(this.environment) && isDBLocal(this.environment);
	}

	private static boolean isDevProfileActivated(Environment environment) {
		return Arrays.stream(environment.getActiveProfiles()).anyMatch(
				env -> "dev".equals(env.toLowerCase())
		);
	}

	private static boolean isDBLocal(Environment environment) {
		// el chequeo es rudimentario para que se cumpla solo en este caso
		return environment.getProperty("spring.datasource.url")
				.equals("jdbc:postgresql://localhost:5432/hospitalDB");
	}


	@Transactional
	public void populateDB() {
		log.warn("Updating sample data");
		sampleProperties.getInstitutions().forEach(institutionInfoSeed -> {
			var address = loadAddress(institutionInfoSeed.getAddress());
			var institution = loadInstitution(institutionInfoSeed, address.getId());
			institutionInfoSeed.getUsers()
					.forEach(userInfoSeed -> populateUser(userInfoSeed, institution.getId()));
		});
		log.warn("Sample data loaded");

	}

	private Institution loadInstitution(InstitutionInfoSeed institutionInfoSeed, Integer addressId) {
		return institutionRepository.findInstitution(institutionInfoSeed.getName(), institutionInfoSeed.getCuit())
				.orElseGet(() -> institutionRepository.save(mapInstitution(institutionInfoSeed, addressId)));
	}


	private Address loadAddress(AddressInfoSeed address) {
		return addressRepository.findAddress(address.getStreet(), address.getNumber(),
				address.getCityId(), address.getPostcode())
				.orElseGet(() -> addressRepository.save(mapAddress(address)));
	}

	private Institution mapInstitution(InstitutionInfoSeed institutionInfoSeed, Integer addressId) {
		var result = new Institution();
		result.setAddressId(addressId);
		result.setName(institutionInfoSeed.getName());
		result.setCuit(institutionInfoSeed.getCuit());
		result.setEmail(institutionInfoSeed.getEmail());
		result.setPhone(institutionInfoSeed.getPhone());
		result.setSisaCode(institutionInfoSeed.getSisaCode());
		return result;
	}

	private Address mapAddress(AddressInfoSeed addressInfoSeed) {
		var address = new Address();
		address.setCityId(addressInfoSeed.getCityId());
		address.setPostcode(addressInfoSeed.getPostcode());
		address.setStreet(addressInfoSeed.getStreet());
		address.setNumber(addressInfoSeed.getNumber());
		return address;
	}

	private void populateUser(UserInfoSeed user, Integer institutionId) {
		userExternalService.getUser(user.getUsername()).ifPresentOrElse(
				userInfoDto ->	userExternalService.enableUser(userInfoDto.getUsername())
				, () -> populateMockUser(user));
		userExternalService.getUser(user.getUsername())
				.ifPresent(userInfoDto -> {
					userAssignmentService.removeAllPermissions(userInfoDto.getId());
					populateUserRoles(user.getRoles(), userInfoDto.getId(), institutionId);
				});
	}


	private void populateMockUser(UserInfoSeed user) {
		userExternalService.registerUser(user.getUsername(), null, user.getPassword());
		userExternalService.enableUser(user.getUsername());
		if (user.getPerson() != null){
			userExternalService.getUser(user.getUsername())
					.ifPresent(userInfoDto -> {
						var newPerson = personRepository.save(mapPerson(user.getPerson()));
						userPersonRepository.save(new UserPerson(userInfoDto.getId(), newPerson.getId()));
						user.getHealthcareProfessionals()
								.forEach(healthcareProfessionalInfoSeed ->
										healthcareProfessionalRepository
												.save(mapHealthProfessional(healthcareProfessionalInfoSeed, newPerson.getId())));

					});

		}
	}

	private Person mapPerson(PersonInfoSeed personSeed) {
		var person = new Person();
		person.setFirstName(personSeed.getFirstName());
		person.setLastName(personSeed.getLastName());
		person.setIdentificationNumber(personSeed.getIdentificationNumber());
		return person;
	}

	private void populateUserRoles(List<UserRoleSeed> roles, Integer userId, Integer institutionId) {
		roles.forEach(userRoleSeed ->
						userAssignmentService.saveUserRole(
								userId,
								ERole.map(userRoleSeed.getRoleId()),
								institutionId));
	}

	private HealthcareProfessional mapHealthProfessional(HealthcareProfessionalInfoSeed hpSeed, Integer personId) {
		var result = new HealthcareProfessional();
		result.setPersonId(personId);
		result.setLicenseNumber(hpSeed.getLicenseNumber());
		return result;
	}

	private <E extends SGXAuditableEntity> List<E> fix(List<E> auditableEntities) {
		return auditableEntities.stream().map(auditableEntity -> {
			fixEntity(auditableEntity);
			return auditableEntity;
		}).collect(Collectors.toList());
	}

	private void fixEntity(SGXAuditableEntity auditableEntity) {
		auditableEntity.initializeAuditableFields();
	}


}
