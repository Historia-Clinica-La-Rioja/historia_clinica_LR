package net.pladema.sgh.app.seeds.data;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("dev")
public class SampleData {

	private static final Logger LOG = LoggerFactory.getLogger(SampleData.class);

	private final SampleProperties sampleProperties;
	private final AddressRepository addressRepository;
	private final InstitutionRepository institutionRepository;
	private final UserExternalService userExternalService;
	private final UserAssignmentService userAssignmentService;
	private final PersonRepository personRepository;
	private final UserPersonRepository userPersonRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	public SampleData(
			SampleProperties sampleProperties,
			InstitutionRepository institutionRepository,
			AddressRepository addressRepository,
			UserExternalService userExternalService,
			UserAssignmentService userAssignmentService,
			PersonRepository personRepository,
			UserPersonRepository userPersonRepository,
			HealthcareProfessionalRepository healthcareProfessionalRepository) {
		this.sampleProperties = sampleProperties;
		this.institutionRepository = institutionRepository;
		this.addressRepository = addressRepository;
		this.userExternalService = userExternalService;
		this.userAssignmentService = userAssignmentService;
		this.personRepository = personRepository;
		this.userPersonRepository = userPersonRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
	}

	@Transactional
	public void populateDB() {
		LOG.warn("Updating sample data");
		sampleProperties.getInstitutions().forEach(institutionInfoSeed -> {
			var address = loadAddress(institutionInfoSeed.getAddress());
			var institution = loadInstitution(institutionInfoSeed, address.getId());
			institutionInfoSeed.getUsers()
					.forEach(userInfoSeed -> populateUser(userInfoSeed, institution.getId()));
		});
		LOG.warn("Sample data loaded");

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
