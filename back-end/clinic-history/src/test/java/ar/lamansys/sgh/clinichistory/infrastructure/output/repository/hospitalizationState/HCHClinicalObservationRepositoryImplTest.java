package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;


import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.clinichistory.mocks.ClinicalObservationTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class HCHClinicalObservationRepositoryImplTest extends UnitRepository {

	private HCHClinicalObservationRepositoryImpl clinicalObservationRepository;

	@MockBean
	private SnomedRepository snomedRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		this.clinicalObservationRepository = new HCHClinicalObservationRepositoryImpl(entityManager);
	}

	@Test
	void saveCreateTest() {
		Integer internmentEpisodeId = 1;
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		createInternmentStates(1, LocalDateTime.parse(date, formatter));

		MapClinicalObservationVo mapClinicalObservationVo = clinicalObservationRepository.getGeneralState(internmentEpisodeId);

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code 1"))
				.isNotNull()
				.isNotEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code 2"))
				.isNotNull()
				.hasSize(1);

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code 3"))
				.isNotNull()
				.isEmpty();

		Assertions.assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code 4"))
				.isNotNull()
				.hasSize(1);
	}


	private void createInternmentStates(Integer internmentEpisodeId, LocalDateTime dateTime){
		Snomed snomed1 = save(SnomedTestMocks.createSnomed("code 1"));
		Document firstDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationRiskFactor riskFactorFinal0 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed1.getId(), dateTime.minusDays(8)));
		ObservationRiskFactor riskFactorFinal1 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed1.getId(), dateTime.plusMinutes(2)));
		ObservationRiskFactor riskFactorError2 = save(ClinicalObservationTestMocks.createErrorObservationRiskFactor(snomed1.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorFinal0));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorFinal1));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(firstDoc, riskFactorError2));


		Snomed snomed2 = save(SnomedTestMocks.createSnomed("code 2"));
		Document secondDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationRiskFactor riskFactorFinal3 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed2.getId(), dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(secondDoc, riskFactorFinal3));

		Snomed snomed3 = save(SnomedTestMocks.createSnomed("code 3"));
		Document thirdDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.ERROR));
		ObservationRiskFactor riskFactorFinal4 = save(ClinicalObservationTestMocks.createFinalObservationRiskFactor(snomed3.getId(), dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentRiskFactor(thirdDoc, riskFactorFinal4));

		Snomed snomed4 = save(SnomedTestMocks.createSnomed("code 4"));
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(snomed4.getId(), dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));
	}

}

