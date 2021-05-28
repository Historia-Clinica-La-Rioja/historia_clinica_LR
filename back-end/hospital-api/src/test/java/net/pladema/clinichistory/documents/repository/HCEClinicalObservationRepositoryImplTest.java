package net.pladema.clinichistory.documents.repository;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.hce.HCEClinicalObservationRepositoryImpl;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEMapClinicalObservationVo;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import net.pladema.clinichistory.mocks.ClinicalObservationTestMocks;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.SourceType;
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

import static net.pladema.clinichistory.mocks.SnomedTestMocks.createSnomed;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HCEClinicalObservationRepositoryImplTest extends UnitRepository {

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
		Snomed snomed1 = save(createSnomed("code1"));
		Document firstDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignError2));


		Snomed snomed2 = save(createSnomed("code2"));
		Document secondDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed2.getId(), dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(secondDoc, vitalSignFinal3));

		Snomed snomed3 = save(createSnomed("code3"));
		Document thirdDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed3.getId(), dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		Snomed snomed4 = save(createSnomed("code4"));
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(snomed4.getId(), dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));

		Snomed snomed5 = save(createSnomed("code5"));
		Document fourthDoc = save(DocumentsTestMocks.createDocument(outpatientId, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal6 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed5.getId(), dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal7 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed5.getId(), dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError8 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(snomed5.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal6));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignFinal7));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(fourthDoc, vitalSignError8));
	}
}
