package net.pladema.clinichistory.hospitalization.application.downloadEpisodeDocument;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.BlobLazyFileBo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;

@Slf4j
@Service
public class DownloadEpisodeDocumentImpl implements DownloadEpisodeDocument {

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final String OUTPUT = "Output -> {}";
	private final String INPUT = "Input parameters -> episodeDocumentId {}";

	public DownloadEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage) {
		this.episodeDocumentStorage = episodeDocumentStorage;
	}

	@Override
	public BlobLazyFileBo run(Integer episodeDocumentId) {
		log.debug(INPUT, episodeDocumentId);
		BlobLazyFileBo bo = episodeDocumentStorage.downloadEpisodeDocument(episodeDocumentId);
		log.debug(OUTPUT, bo);
		return bo;
	}
}
