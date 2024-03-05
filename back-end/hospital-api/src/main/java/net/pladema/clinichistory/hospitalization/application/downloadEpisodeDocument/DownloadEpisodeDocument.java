package net.pladema.clinichistory.hospitalization.application.downloadEpisodeDocument;


import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.BlobLazyFileBo;

public interface DownloadEpisodeDocument {

	BlobLazyFileBo run(Integer episodeDocumentId);
}
