package net.pladema.clinichistory.hospitalization.repository;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class InternmentEpisodeRepositoryTest extends UnitRepository {

	@Autowired
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@Before
	public void setUp() {
	}

	@Test
	public void canCreateEpicrisis_thenTrue() {

		InternmentEpisode internmentEpisode = createInternmentEpisode();
		save(internmentEpisode);

		Document anamnesis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.ANAMNESIS, SourceType.INTERNACION, DocumentStatus.FINAL);
		anamnesis = save(anamnesis);

		Document evolutionNote = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EVALUATION_NOTE, SourceType.INTERNACION, DocumentStatus.FINAL);
		evolutionNote = save(evolutionNote);

		save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));

		internmentEpisode.setAnamnesisDocId(anamnesis.getId());
		save(internmentEpisode);

		assertTrue(internmentEpisodeRepository.canCreateEpicrisis(internmentEpisode.getId()));

	}


	@Test
	public void canCreateEpicrisis_thenFalse() {

		InternmentEpisode internmentEpisode = createInternmentEpisode();
		save(internmentEpisode);

		Document anamnesis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.ANAMNESIS, SourceType.INTERNACION, DocumentStatus.FINAL);
		anamnesis = save(anamnesis);

		Document evolutionNote = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EVALUATION_NOTE, SourceType.INTERNACION, DocumentStatus.FINAL);
		evolutionNote = save(evolutionNote);

		save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));

		Document epicrisis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EPICRISIS, SourceType.INTERNACION, DocumentStatus.FINAL);
		epicrisis = save(epicrisis);

		internmentEpisode.setAnamnesisDocId(anamnesis.getId());
		internmentEpisode.setEpicrisisDocId(epicrisis.getId());
		save(internmentEpisode);

		assertFalse(internmentEpisodeRepository.canCreateEpicrisis(internmentEpisode.getId()));

	}

	private InternmentEpisode createInternmentEpisode() {
		InternmentEpisode result = new InternmentEpisode();
		result.setInstitutionId(1);
		result.setPatientId(1);
		result.setStatusId(InternmentEpisodeStatus.ACTIVE_ID);
		result.setBedId(1);
		return result;
	}


}
