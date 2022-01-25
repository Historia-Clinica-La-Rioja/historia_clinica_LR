package net.pladema.clinichistory.hospitalization.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class InternmentEpisodeRepositoryTest extends UnitRepository {

	@Autowired
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@BeforeEach
	void setUp() {
	}

	@Test
	void canCreateEpicrisis_thenTrue() {

		InternmentEpisode internmentEpisode = createInternmentEpisode();
		save(internmentEpisode);

		Document anamnesis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL);
		anamnesis = save(anamnesis);

		Document evolutionNote = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION, DocumentStatus.FINAL);
		evolutionNote = save(evolutionNote);

		save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));

		internmentEpisode.setAnamnesisDocId(anamnesis.getId());
		save(internmentEpisode);

		Assertions.assertTrue(internmentEpisodeRepository.canCreateEpicrisis(internmentEpisode.getId()));

	}


	@Test
	void canCreateEpicrisis_thenFalse() {

		InternmentEpisode internmentEpisode = createInternmentEpisode();
		save(internmentEpisode);

		Document anamnesis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL);
		anamnesis = save(anamnesis);

		Document evolutionNote = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION, DocumentStatus.FINAL);
		evolutionNote = save(evolutionNote);

		save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));

		Document epicrisis = DocumentsTestMocks.createDocument(internmentEpisode.getId(), DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL);
		epicrisis = save(epicrisis);

		internmentEpisode.setAnamnesisDocId(anamnesis.getId());
		internmentEpisode.setEpicrisisDocId(epicrisis.getId());
		save(internmentEpisode);

		Assertions.assertFalse(internmentEpisodeRepository.canCreateEpicrisis(internmentEpisode.getId()));

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
