package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

class AllergyIntoleranceRepositoryTest extends UnitRepository {

	@Autowired
	private AllergyIntoleranceRepository allergyIntoleranceRepository;

	@MockBean
	private SnomedRepository snomedRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void saveCreateTest() {
		AllergyIntolerance allergy = createMinimumAllergyEntity();

		assertThat(allergy.getCreatedOn())
				.isNull();

		assertThat(allergy.getUpdatedOn())
				.isNull();

		assertThat(allergy.getCreatedBy())
				.isNull();

		assertThat(allergy.getUpdatedBy())
				.isNull();

		allergy = allergyIntoleranceRepository.save(allergy);

		assertThat(allergy.getCreatedOn())
				.isNotNull();

		assertThat(allergy.getUpdatedOn())
				.isNotNull();

		assertThat(allergy.getCreatedBy())
				.isNotNull();

		assertThat(allergy.getUpdatedBy())
				.isNotNull();
	}

	private AllergyIntolerance createMinimumAllergyEntity() {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setPatientId(1);
		allergy.setSnomedId(1);
		allergy.setStatusId("status");
		allergy.setVerificationStatusId("verification");
		allergy.setCategoryId(null);
		return allergy;
	}
}
