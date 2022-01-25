package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;


import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationVitalSign;
import ar.lamansys.sgh.clinichistory.mocks.ClinicalObservationTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
class HCEClinicalObservationRepositoryImplTest extends UnitRepository {

	private HCEClinicalObservationRepositoryImpl hceClinicalObservationRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		hceClinicalObservationRepository = new HCEClinicalObservationRepositoryImpl(entityManager);
	}

	@Test
	void test_hce_vital_sign_condition_success() {

		Integer outpatientId = 1;
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		createOutpatientStates(outpatientId, LocalDateTime.parse(date, formatter));

		HCEMapClinicalObservationVo mapClinicalObservationVo = hceClinicalObservationRepository.getGeneralState(outpatientId);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code1"))
				.isNotNull()
				.isNotEmpty();

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code2"))
				.isNotNull()
				.hasSize(1);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code3"))
				.isNotNull()
				.isEmpty();

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code4"))
				.isNotNull()
				.hasSize(1);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code5"))
				.isNotNull()
				.isEmpty();
	}


	private void createOutpatientStates(Integer outpatientId, LocalDateTime dateTime){
		Snomed snomed1 = save(SnomedTestMocks.createSnomed("code1"));
		Document firstDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignError2));


		Snomed snomed2 = save(SnomedTestMocks.createSnomed("code2"));
		Document secondDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed2.getId(), dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(secondDoc, vitalSignFinal3));

		Snomed snomed3 = save(SnomedTestMocks.createSnomed("code3"));
		Document thirdDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed3.getId(), dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		Snomed snomed4 = save(SnomedTestMocks.createSnomed("code4"));
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(snomed4.getId(), dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));

		Snomed snomed5 = save(SnomedTestMocks.createSnomed("code5"));
		Document fourthDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal6 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed5.getId(), dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal7 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed5.getId(), dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError8 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(snomed5.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal6));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal7));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignError8));
	}
}
