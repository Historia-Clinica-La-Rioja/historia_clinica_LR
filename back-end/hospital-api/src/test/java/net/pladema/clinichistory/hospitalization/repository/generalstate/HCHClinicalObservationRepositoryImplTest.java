package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.BaseRepositoryTest;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.ips.repository.entity.ObservationLab;
import net.pladema.clinichistory.ips.repository.entity.ObservationVitalSign;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.service.domain.MapClinicalObservationVo;
import net.pladema.clinichistory.mocks.ClinicalObservationTestMocks;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HCHClinicalObservationRepositoryImplTest extends BaseRepositoryTest {

	private HCHClinicalObservationRepositoryImpl clinicalObservationRepository;

	@Autowired
	private EntityManager entityManager;

	@Before
	public void setUp() {
		this.clinicalObservationRepository = new HCHClinicalObservationRepositoryImpl(entityManager);
	}

	@Test
	public void saveCreateTest() {
		Integer internmentEpisodeId = 1;
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		createInternmentStates(1, LocalDateTime.parse(date, formatter));

		MapClinicalObservationVo mapClinicalObservationVo = clinicalObservationRepository.getGeneralState(internmentEpisodeId);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code1"))
				.isNotNull()
				.isNotEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code2"))
				.isNotNull()
				.hasSize(1);

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code3"))
				.isNotNull()
				.isEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code4"))
				.isNotNull()
				.hasSize(1);
	}


	private void createInternmentStates(Integer internmentEpisodeId, LocalDateTime dateTime){
		String code1 = "code1";
		Document firstDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.INTERNACION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code1, dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code1, dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(code1, dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignError2));


		String code2 = "code2";
		Document secondDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.INTERNACION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code2, dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(secondDoc, vitalSignFinal3));

		String code3 = "code3";
		Document thirdDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.INTERNACION, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code3, dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		String code4 = "code4";
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(code4, dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));
	}


}

