package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;
import net.pladema.clinichistory.documents.service.ips.domain.MapClinicalObservationVo;
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
public class HCHClinicalObservationRepositoryImplTest extends UnitRepository {

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
		Snomed snomed1 = save(createSnomed("code 1"));
		Document firstDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(ClinicalObservationTestMocks.createErrorObservationVitalSign(snomed1.getId(), dateTime.plusMinutes(5)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(firstDoc, vitalSignError2));


		Snomed snomed2 = save(createSnomed("code 2"));
		Document secondDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed2.getId(), dateTime.plusMinutes(6)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(secondDoc, vitalSignFinal3));

		Snomed snomed3 = save(createSnomed("code 3"));
		Document thirdDoc = save(DocumentsTestMocks.createDocument(internmentEpisodeId, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(ClinicalObservationTestMocks.createFinalObservationVitalSign(snomed3.getId(), dateTime.plusMinutes(7)));
		save(ClinicalObservationTestMocks.createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		Snomed snomed4 = save(createSnomed("code 4"));
		ObservationLab observationLab = save(ClinicalObservationTestMocks.createFinalObservationLab(snomed4.getId(), dateTime.plusMinutes(8)));
		save(ClinicalObservationTestMocks.createDocumentLab(secondDoc, observationLab));
	}

}

