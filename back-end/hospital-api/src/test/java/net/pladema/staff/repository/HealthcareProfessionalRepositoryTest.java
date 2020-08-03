package net.pladema.staff.repository;

import net.pladema.UnitRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.repository.mocks.StaffTestMocks;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HealthcareProfessionalRepositoryTest extends UnitRepository {

	@Autowired
	private HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_exist_professional() {
		Person person = save(StaffTestMocks.createMinimumPerson("Juan", "Perez", "1234"));

		HealthcareProfessional hp = save(StaffTestMocks.createMinimumProfessional(person.getId(), "1234/5"));

		Optional<HealthcareProfessionalVo> optResultQuery = healthcareProfessionalRepository.findProfessionalById(hp.getId());

		Assertions.assertThat(optResultQuery.isPresent())
				.isTrue();

		HealthcareProfessionalVo resultQuery = optResultQuery.get();

		Assertions.assertThat(resultQuery)
				.isNotNull();

		Assertions.assertThat(resultQuery.getFirstName())
				.isEqualTo("Juan");

		Assertions.assertThat(resultQuery.getLastName())
				.isEqualTo("Perez");

		Assertions.assertThat(resultQuery.getIdentificationNumber())
				.isEqualTo("1234");

		Assertions.assertThat(resultQuery.getLicenceNumber())
				.isEqualTo("1234/5");
	}


	@Test
	public void test_not_exist_professional() {
		Optional<HealthcareProfessionalVo> optResultQuery = healthcareProfessionalRepository.findProfessionalById(4);

		Assertions.assertThat(optResultQuery.isPresent())
				.isFalse();
	}
}
