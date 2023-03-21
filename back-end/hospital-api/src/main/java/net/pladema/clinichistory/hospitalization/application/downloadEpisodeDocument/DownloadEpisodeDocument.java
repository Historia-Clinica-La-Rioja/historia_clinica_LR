package net.pladema.clinichistory.hospitalization.application.downloadEpisodeDocument;

import net.pladema.clinichistory.hospitalization.service.domain.StoredFileBo;

public interface DownloadEpisodeDocument {

	StoredFileBo run(Integer episodeDocumentId);
}
