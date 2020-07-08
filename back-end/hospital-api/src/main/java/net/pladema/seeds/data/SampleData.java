package net.pladema.seeds.data;

import net.pladema.address.repository.AddressRepository;
import net.pladema.sgx.auditable.entity.Audit;
import net.pladema.sgx.auditable.entity.AuditableEntity;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.user.repository.UserPasswordRepository;
import net.pladema.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleData {

	private static final Logger LOG = LoggerFactory.getLogger(SampleData.class);

	private final SampleProperties sampleProperties;
	private final AddressRepository addressRepository;
	private final InstitutionRepository institutionRepository;
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserPasswordRepository userPasswordRepository;
	private final PersonRepository personRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	public SampleData(
			SampleProperties sampleProperties,
			InstitutionRepository institutionRepository,
			AddressRepository addressRepository,
			UserRepository userRepository,
			UserRoleRepository userRoleRepository,
			UserPasswordRepository userPasswordRepository,
			PersonRepository personRepository,
			HealthcareProfessionalRepository healthcareProfessionalRepository) {
		this.sampleProperties = sampleProperties;
		this.institutionRepository = institutionRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.userPasswordRepository = userPasswordRepository;
		this.personRepository = personRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
	}

	public void populateDB() {
		LOG.warn("Updating sample data");
		addressRepository.saveAll(sampleProperties.getAddresses());
		institutionRepository.saveAll(sampleProperties.getInstitutions());
		personRepository.saveAll(sampleProperties.getPeople());
		healthcareProfessionalRepository.saveAll(sampleProperties.getHealthcareProfessionals());
		userRepository.saveAll(fix(sampleProperties.getUsers()));
		userRoleRepository.saveAll(fix(sampleProperties.getUserRoles()));
		userPasswordRepository.saveAll(fix(sampleProperties.getUserPasswords()));
	}

	private <E extends AuditableEntity> List<E> fix(List<E> auditableEntities) {
		return auditableEntities.stream().map(auditableEntity -> {
			auditableEntity.setAudit(new Audit());
			return auditableEntity;
		}).collect(Collectors.toList());
	}


}
