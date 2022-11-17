package net.pladema.staff.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthcareProfessionalServiceImplTest {

	private HealthcareProfessionalServiceImpl healthcareProfessionalService;

	@Mock
	private HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

	@Mock
	private HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Mock
	private ProfessionalProfessionRepository professionalProfessionRepository;

	@Mock
	private HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

	@BeforeEach
	public void setUp() {
		healthcareProfessionalService = new HealthcareProfessionalServiceImpl(healthcareProfessionalGroupRepository,
				healthcareProfessionalRepository,
				professionalProfessionRepository,
				healthcareProfessionalSpecialtyRepository);
	}

	@Test
	void existProfessional() {

		when(healthcareProfessionalRepository.findActiveProfessionalById(any()))
				.thenReturn(Optional.of(new HealthcareProfessionalVo(1, "1234/5", "Juan", "Perez", "1234",1, "Calos")));

		HealthcareProfessionalBo resultService = healthcareProfessionalService.findActiveProfessionalById(1);

		Assertions.assertNotNull(resultService);

		Assertions.assertEquals("Juan", resultService.getFirstName());
		Assertions.assertEquals("Perez", resultService.getLastName());
		Assertions.assertEquals("1234", resultService.getIdentificationNumber());
		Assertions.assertEquals("1234/5", resultService.getLicenceNumber());
	}


	@Test
	void notExistProfessional() {

		when(healthcareProfessionalRepository.findActiveProfessionalById(any()))
				.thenReturn(Optional.empty());

		Assertions.assertThrows(NotFoundException.class, () ->	healthcareProfessionalService.findActiveProfessionalById(1));

	}

	@Test
	void successFetchAllProfessional() {

		when(healthcareProfessionalRepository.getAllProfessional())
				.thenReturn(List.of(new HealthcareProfessionalVo(1, "1234/5", "Juan", "Perez", "1234",1, "Carlos")));

		var result = healthcareProfessionalService.getAllProfessional();

		Assertions.assertNotNull(result);

		Assertions.assertEquals("Juan", result.get(0).getFirstName());
		Assertions.assertEquals("Perez", result.get(0).getLastName());
		Assertions.assertEquals("1234", result.get(0).getIdentificationNumber());
		Assertions.assertEquals("1234/5", result.get(0).getLicenceNumber());
	}
}
