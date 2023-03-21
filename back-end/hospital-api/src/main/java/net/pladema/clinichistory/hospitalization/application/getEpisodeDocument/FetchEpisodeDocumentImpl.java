package net.pladema.clinichistory.hospitalization.application.getEpisodeDocument;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FetchEpisodeDocumentImpl implements FetchEpisodeDocument {

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final String OUTPUT = "Output -> {}";
	private final String INPUT = "Input parameters -> internmentEpisodeId {}";

	public FetchEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage) {
		this.episodeDocumentStorage = episodeDocumentStorage;
	}

	@Override
	public List<EpisodeDocumentResponseBo> run(Integer internmentEpisodeId) {
		log.debug(INPUT, internmentEpisodeId);
		List<EpisodeDocumentResponseBo> result = episodeDocumentStorage.getEpisodeDocuments(internmentEpisodeId);
		log.debug(OUTPUT, result);
		return result;
	}
}
