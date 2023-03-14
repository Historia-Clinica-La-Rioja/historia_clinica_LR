package net.pladema.clinichistory.hospitalization.application.port;

import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;
import net.pladema.clinichistory.hospitalization.service.domain.SavedEpisodeDocumentResponseBo;
import net.pladema.clinichistory.hospitalization.service.domain.StoredFileBo;

import java.util.List;

public interface EpisodeDocumentStorage {

	Integer saveEpisodeDocument(String partialPath, String uuid, EpisodeDocumentBo bo);

	List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId);

	List<DocumentTypeBo> getDocumentTypes();

	boolean deleteDocument(Integer episodeDocumentId);

	StoredFileBo downloadEpisodeDocument(Integer episodeDocumentId);
}
