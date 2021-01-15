package net.pladema.clinichistory.documents.repository.ips;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.AllergyIntolerance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class AllergyIntoleranceRepositoryTest extends UnitRepository {

	@Autowired
	private AllergyIntoleranceRepository allergyIntoleranceRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveCreateTest() {
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
		allergy.setCategoryId("category");
		return allergy;
	}
}
