package net.pladema.clinichistory.hospitalization.application.deleteEpisodeDocument;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteEpisodeDocumentImpl implements DeleteEpisodeDocument {

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final String OUTPUT = "Output -> {}";
	private final String INPUT = "Input parameters -> episodeDocumentId {}";

	public DeleteEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage) {
		this.episodeDocumentStorage = episodeDocumentStorage;
	}

	@Override
	public Boolean run(Integer episodeDocumentId) {
		log.debug(INPUT, episodeDocumentId);
		Boolean result = episodeDocumentStorage.deleteDocument(episodeDocumentId);
		log.debug(OUTPUT, result);
		return result;
	}
}
