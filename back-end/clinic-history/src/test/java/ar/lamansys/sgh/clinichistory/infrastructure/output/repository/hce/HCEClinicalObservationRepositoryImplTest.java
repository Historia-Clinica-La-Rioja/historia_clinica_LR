package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;


import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.clinichistory.mocks.ClinicalObservationTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
class HCEClinicalObservationRepositoryImplTest extends UnitRepository {

	private HCEClinicalObservationRepositoryImpl hceClinicalObservationRepository;

	@MockBean
	private SnomedRepository snomedRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		hceClinicalObservationRepository = new HCEClinicalObservationRepositoryImpl(entityManager);
	}

	@Test
	void test_hce_risk_factor_condition_success() {

		Integer outpatientId = 1;
		List<Short> invalidDocumentTypes = Arrays.asList(DocumentType.ANAMNESIS, DocumentType.EVALUATION_NOTE, DocumentType.EPICRISIS);
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		createOutpatientStates(outpatientId, LocalDateTime.parse(date, formatter));

		HCEMapClinicalObservationVo mapClinicalObservationVo = hceClinicalObservationRepository.getGeneralState(outpatientId, invalidDocumentTypes);

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
		ObservationRiskFactor riskFactorFinal0 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed1.getId(), dateTime.minusDays(8)));
		ObservationRiskFactor riskFactorFinal1 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed1.getId(), dateTime.plusMinutes(2)));
		ObservationRiskFactor riskFactorError2 = save(ClinicalObservationTestMocks.createErrorObservationRiskFactor(snomed1.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorFinal0));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorFinal1));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorError2));


		Snomed snomed2 = save(SnomedTestMocks.createSnomed("code2"));
		Document secondDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL));
		ObservationRiskFactor riskFactorFinal3 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed2.getId(), dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(secondDoc, riskFactorFinal3));

		Snomed snomed3 = save(SnomedTestMocks.createSnomed("code3"));
		Document thirdDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.ERROR));
		ObservationRiskFactor riskFactorFinal4 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed3.getId(), dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(thirdDoc, riskFactorFinal4));

		Snomed snomed4 = save(SnomedTestMocks.createSnomed("code4"));
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(snomed4.getId(), dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));

		Snomed snomed5 = save(SnomedTestMocks.createSnomed("code5"));
		Document fourthDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationRiskFactor riskFactorFinal6 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed5.getId(), dateTime.minusDays(8)));
		ObservationRiskFactor riskFactorFinal7 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed5.getId(), dateTime.plusMinutes(2)));
		ObservationRiskFactor riskFactorError8 = save(ClinicalObservationTestMocks.createErrorObservationRiskFactor(snomed5.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(fourthDoc, riskFactorFinal6));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(fourthDoc, riskFactorFinal7));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(fourthDoc, riskFactorError8));
	}
}
