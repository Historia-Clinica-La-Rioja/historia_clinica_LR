package net.pladema.clinichistory.hospitalization.application.getDocumentType;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FetchDocumentTypeImpl implements FetchDocumentType {

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final String OUTPUT = "Output -> {}";

	public FetchDocumentTypeImpl(EpisodeDocumentStorage episodeDocumentStorage) {
		this.episodeDocumentStorage = episodeDocumentStorage;
	}

	@Override
	public List<DocumentTypeBo> run() {
		List<DocumentTypeBo> result = episodeDocumentStorage.getDocumentTypes();
		log.debug(OUTPUT, result);
		return result;
	}
}
