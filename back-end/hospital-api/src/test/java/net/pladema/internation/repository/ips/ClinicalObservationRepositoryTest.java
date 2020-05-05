package net.pladema.internation.repository.ips;

import net.pladema.BaseRepositoryTest;
import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentVitalSign;
import net.pladema.internation.repository.ips.entity.ObservationLab;
import net.pladema.internation.repository.ips.entity.ObservationVitalSign;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.ips.domain.MapClinicalObservationVo;
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
public class ClinicalObservationRepositoryTest extends BaseRepositoryTest {

	private ClinicalObservationRepositoryImpl clinicalObservationRepository;

	@Autowired
	private EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
		clinicalObservationRepository = new ClinicalObservationRepositoryImpl(entityManager);
	}


	@Test
	public void saveCreateTest() {
		Integer internmentEpisodeId = 1;
		createInternmentStates(1);
		MapClinicalObservationVo mapClinicalObservationVo = clinicalObservationRepository.getGeneralStateLastSevenDays(internmentEpisodeId);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code1"))
				.isNotNull()
				.isEmpty();

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code2"))
				.isNotNull()
				.hasSize(1);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code3"))
				.isNotNull()
				.hasSize(1);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode("code4"))
				.isNotNull()
				.hasSize(1);
	}


	private void createInternmentStates(Integer internmentEpisodeId){
		String code1 = "code1";
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		Document firstDoc = save(createDocument(internmentEpisodeId, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(createFinalObservationVitalSign(code1, dateTime.minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(createFinalObservationVitalSign(code1, dateTime.plusMinutes(2)));
		ObservationVitalSign vitalSignError2 = save(createErrorObservationVitalSign(code1, dateTime.plusMinutes(5)));
		save(createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(createDocumentVitalSign(firstDoc, vitalSignError2));


		String code2 = "code2";
		Document secondDoc = save(createDocument(internmentEpisodeId, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(createFinalObservationVitalSign(code2, dateTime.plusMinutes(6)));
		save(createDocumentVitalSign(secondDoc, vitalSignFinal3));

		String code3 = "code3";
		Document thirdDoc = save(createDocument(internmentEpisodeId, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(createFinalObservationVitalSign(code3, dateTime.plusMinutes(7)));
		save(createDocumentVitalSign(thirdDoc, vitalSignFinal4));

		String code4 = "code4";
		ObservationLab observationLab = save(createFinalObservationLab(code4, dateTime.plusMinutes(8)));
		save(createDocumentLab(secondDoc, observationLab));
	}


	private DocumentVitalSign createDocumentVitalSign(Document doc, ObservationVitalSign obs){
		return new DocumentVitalSign(doc.getId(), obs.getId());
	}

	private ObservationVitalSign createFinalObservationVitalSign(String sctId, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = createObservationVitalSign(sctId, ObservationStatus.FINAL, now);
		return observationVitalSign;
	}

	private ObservationVitalSign createErrorObservationVitalSign(String sctId, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = createObservationVitalSign(sctId, ObservationStatus.ERROR, now);
		return observationVitalSign;
	}

	private DocumentLab createDocumentLab(Document doc, ObservationLab obs){
		return new DocumentLab(doc.getId(), obs.getId());
	}

	private ObservationLab createFinalObservationLab(String sctId, LocalDateTime now) {
		ObservationLab observationLab = createObservationLab(sctId, ObservationStatus.FINAL, now);
		return observationLab;
	}

	private ObservationLab createObservationLab(String sctId, String error, LocalDateTime now) {
		ObservationLab result = new ObservationLab();
		result.setPatientId(1);
		result.setSctidCode(sctId);
		result.setStatusId(error);
		result.setCategoryId("category");
		result.setValue("Lab Value");
		result.setEffectiveTime(now);
		return result;
	}

	private ObservationVitalSign createObservationVitalSign(String sctId, String error, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = new ObservationVitalSign();
		observationVitalSign.setPatientId(1);
		observationVitalSign.setSctidCode(sctId);
		observationVitalSign.setStatusId(error);
		observationVitalSign.setCategoryId("category");
		observationVitalSign.setValue("Vital sign Value");
		observationVitalSign.setEffectiveTime(now);
		return observationVitalSign;
	}

	private Document createDocument(Integer intermentEpisodeId, String status) {
		Document document = new Document();
		document.setInternmentEpisodeId(intermentEpisodeId);
		document.setStatusId(status);
		document.setTypeId(DocumentType.ANAMNESIS);
		return document;
	}
}

