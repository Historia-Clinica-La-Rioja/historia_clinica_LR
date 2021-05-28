package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HealthcareProfessionalServiceImplTest {

	private HealthcareProfessionalServiceImpl healthcareProfessionalService;

	@MockBean
	private HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

	@MockBean
	private HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Before
	public void setUp() {
		healthcareProfessionalService = new HealthcareProfessionalServiceImpl(healthcareProfessionalGroupRepository, healthcareProfessionalRepository);
	}

	@Test
	public void test_exist_professional() {

		when(healthcareProfessionalRepository.findProfessionalById(any()))
				.thenReturn(Optional.of(new HealthcareProfessionalVo(1, "1234/5", "Juan", "Perez", "1234")));

		HealthcareProfessionalBo resultService = healthcareProfessionalService.findProfessionalById(1);

		Assertions.assertThat(resultService)
				.isNotNull();

		Assertions.assertThat(resultService.getFirstName())
				.isEqualTo("Juan");

		Assertions.assertThat(resultService.getLastName())
				.isEqualTo("Perez");

		Assertions.assertThat(resultService.getIdentificationNumber())
				.isEqualTo("1234");

		Assertions.assertThat(resultService.getLicenceNumber())
				.isEqualTo("1234/5");
	}


	@Test(expected = NotFoundException.class)
	public void test_not_exist_professional() {

		when(healthcareProfessionalRepository.findProfessionalById(any()))
				.thenReturn(Optional.empty());

		healthcareProfessionalService.findProfessionalById(1);
	}
}
