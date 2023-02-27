package net.pladema.clinichistory.hospitalization.application.downloadEpisodeDocument;


import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

public interface DownloadEpisodeDocument {

	StoredFileBo run(Integer episodeDocumentId);
}
