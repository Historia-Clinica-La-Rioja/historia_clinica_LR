package net.pladema.clinichistory.generalstate.repository;

import net.pladema.BaseRepositoryTest;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.generalstate.repository.domain.HCEMapClinicalObservationVo;
import net.pladema.clinichistory.ips.repository.entity.ObservationLab;
import net.pladema.clinichistory.ips.repository.entity.ObservationVitalSign;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
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
public class HCEClinicalObservationRepositoryImplTest extends BaseRepositoryTest {

	private HCEClinicalObservationRepositoryImpl hceClinicalObservationRepository;

	@Autowired
	private EntityManager entityManager;

	@Before
	public void setUp() {
		hceClinicalObservationRepository = new HCEClinicalObservationRepositoryImpl(entityManager);
	}

	@Test
	public void test_hce_vital_sign_condition_success() {

		Integer outpatientId = 1;
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		createOutpatientStates(outpatientId, LocalDateTime.parse(date, formatter));

		HCEMapClinicalObservationVo mapClinicalObservationVo = hceClinicalObservationRepository.getGeneralState(outpatientId);

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

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code5"))
				.isNotNull()
				.isEmpty();
	}


	private void createOutpatientStates(Integer outpatientId, LocalDateTime dateTime){
		String code1 = "code1";
		Document firstDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.AMBULATORIA, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code1, dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code1, dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(code1, dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignError2));


		String code2 = "code2";
		Document secondDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.AMBULATORIA, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code2, dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(secondDoc, vitalSignFinal3));

		String code3 = "code3";
		Document thirdDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.AMBULATORIA, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code3, dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		String code4 = "code4";
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(code4, dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));

		String code5 = "code5";
		Document fourthDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.EPICRISIS, SourceType.INTERNACION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal6 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code5, dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal7 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(code5, dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError8 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(code5, dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal6));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal7));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignError8));
	}
}
