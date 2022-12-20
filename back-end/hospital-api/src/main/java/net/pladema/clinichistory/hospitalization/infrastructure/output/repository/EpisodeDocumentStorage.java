package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;
import net.pladema.clinichistory.hospitalization.service.domain.SavedEpisodeDocumentResponseBo;

import java.util.List;

public interface EpisodeDocumentStorage {

	SavedEpisodeDocumentResponseBo saveEpisodeDocument(EpisodeDocumentBo bo);

	List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId);

	List<DocumentTypeBo> getDocumentTypes();
}
