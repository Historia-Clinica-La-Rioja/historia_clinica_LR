package net.pladema.internation.repository.ips;

import net.pladema.BaseRepositoryTest;
import net.pladema.internation.repository.ips.entity.AllergyIntolerance;
import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.time.LocalDate;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createLicense;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class AllergyIntoleranceRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private AllergyIntoleranceRepository allergyIntoleranceRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveCreateTest() {
		AllergyIntolerance allergy = createMinimunAllergyEntity();

		assertThat(allergy.getCreatedOn())
				.isNull();

		assertThat(allergy.getUpdatedOn())
				.isNull();

		assertThat(allergy.getCreatedBy())
				.isNull();

		assertThat(allergy.getModifiedBy())
				.isNull();

		allergy = allergyIntoleranceRepository.save(allergy);

		assertThat(allergy.getCreatedOn())
				.isNotNull();

		assertThat(allergy.getUpdatedOn())
				.isNotNull();

		assertThat(allergy.getCreatedBy())
				.isNotNull();

		assertThat(allergy.getModifiedBy())
				.isNotNull();
	}

	private AllergyIntolerance createMinimunAllergyEntity() {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setPatientId(1);
		allergy.setSctidCode("code");
		allergy.setStatusId("status");
		allergy.setVerificationStatusId("verification");
		allergy.setCategoryId("category");
		return allergy;
	}
}
