package net.pladema.internation.repository.ips;

import net.pladema.BaseRepositoryTest;
import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.core.entity.DocumentVitalSign;
import net.pladema.internation.repository.ips.entity.ObservationVitalSign;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.domain.ips.MapVitalSigns;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class ObservationVitalSignRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private ObservationVitalSignRepository observationVitalSignRepository;

	@Before
	public void setUp() throws Exception {
	}


	@Test
	public void saveCreateTest() {
		Integer internmentEpisodeId = 1;
		createInternmentStates(1);
		MapVitalSigns mapVitalSigns = observationVitalSignRepository.getVitalSignsGeneralStateLastSevenDays(internmentEpisodeId);

		assertThat(mapVitalSigns.getGroupByVitalSign().entrySet())
				.isNotNull()
				.isNotEmpty();

		assertThat(mapVitalSigns.getVitalSignsByCode("code1"))
				.isNotNull()
				.isEmpty();

		assertThat(mapVitalSigns.getVitalSignsByCode("code2"))
				.isNotNull()
				.hasSize(1);

		assertThat(mapVitalSigns.getVitalSignsByCode("code3"))
				.isNotNull()
				.hasSize(1);
	}


	private void createInternmentStates(Integer internmentEpisodeId){
		String code1 = "code1";
		Document firstDoc = save(createDocument(internmentEpisodeId, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal0 = save(createFinalObservationVitalSign(code1, LocalDateTime.now().minusDays(8)));
		ObservationVitalSign vitalSignFinal1 = save(createFinalObservationVitalSign(code1, LocalDateTime.now()));
		ObservationVitalSign vitalSignError2 = save(createErrorObservationVitalSign(code1, LocalDateTime.now()));
		save(createDocumentVitalSign(firstDoc, vitalSignFinal0));
		save(createDocumentVitalSign(firstDoc, vitalSignFinal1));
		save(createDocumentVitalSign(firstDoc, vitalSignError2));


		String code2 = "code2";
		Document secondDoc = save(createDocument(internmentEpisodeId, DocumentStatus.FINAL));
		ObservationVitalSign vitalSignFinal3 = save(createFinalObservationVitalSign(code2, LocalDateTime.now()));
		save(createDocumentVitalSign(secondDoc, vitalSignFinal3));

		String code3 = "code3";
		Document thirdDoc = save(createDocument(internmentEpisodeId, DocumentStatus.ERROR));
		ObservationVitalSign vitalSignFinal4 = save(createFinalObservationVitalSign(code3, LocalDateTime.now()));
		save(createDocumentVitalSign(thirdDoc, vitalSignFinal4));
	}


	private DocumentVitalSign createDocumentVitalSign(Document doc, ObservationVitalSign obs){
		return new DocumentVitalSign(doc.getId(), obs.getId());
	}

	private ObservationVitalSign createFinalObservationVitalSign(String sctid, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = createObservationVitalSign(sctid, ObservationStatus.FINAL, now);
		return observationVitalSign;
	}

	private ObservationVitalSign createErrorObservationVitalSign(String sctid, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = createObservationVitalSign(sctid, ObservationStatus.ERROR, now);
		return observationVitalSign;
	}

	private ObservationVitalSign createObservationVitalSign(String sctid, String error, LocalDateTime now) {
		ObservationVitalSign observationVitalSign = new ObservationVitalSign();
		observationVitalSign.setPatientId(1);
		observationVitalSign.setSctidCode(sctid);
		observationVitalSign.setStatusId(error);
		observationVitalSign.setCategoryId("category");
		observationVitalSign.setValue("Value");
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

