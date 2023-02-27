package net.pladema.clinichistory.hospitalization.application.port;

import java.util.List;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

public interface EpisodeDocumentStorage {

	Integer saveEpisodeDocument(String partialPath, String uuid, EpisodeDocumentBo bo);

	List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId);

	List<DocumentTypeBo> getDocumentTypes();

	boolean deleteDocument(Integer episodeDocumentId);

	StoredFileBo downloadEpisodeDocument(Integer episodeDocumentId);
}
