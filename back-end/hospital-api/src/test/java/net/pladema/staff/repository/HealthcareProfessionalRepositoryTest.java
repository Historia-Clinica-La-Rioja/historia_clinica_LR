package net.pladema.staff.repository;

import net.pladema.UnitRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.repository.mocks.StaffTestMocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class HealthcareProfessionalRepositoryTest extends UnitRepository {

	@Autowired
	private HealthcareProfessionalRepository healthcareProfessionalRepository;

	@BeforeEach
	void setUp() {
	}

	@Test
	void test_exist_professional() {
		Person person = save(StaffTestMocks.createMinimumPerson("Juan", "Perez", "1234"));

		HealthcareProfessional hp = save(StaffTestMocks.createMinimumProfessional(person.getId(), "1234/5"));

		Optional<HealthcareProfessionalVo> optResultQuery = healthcareProfessionalRepository.findActiveProfessionalById(hp.getId());

		Assertions.assertTrue(optResultQuery.isPresent());

		HealthcareProfessionalVo resultQuery = optResultQuery.get();

		Assertions.assertNotNull(resultQuery);

		Assertions.assertEquals("Juan", resultQuery.getFirstName());

		Assertions.assertEquals("Perez", resultQuery.getLastName());

		Assertions.assertEquals("1234", resultQuery.getIdentificationNumber());

		Assertions.assertEquals("1234/5", resultQuery.getLicenceNumber());
	}


	@Test
	void test_not_exist_professional() {
		Optional<HealthcareProfessionalVo> optResultQuery = healthcareProfessionalRepository.findActiveProfessionalById(4);

		Assertions.assertFalse(optResultQuery.isPresent());
	}
}
