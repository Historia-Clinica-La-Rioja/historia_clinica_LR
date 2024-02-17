package net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentTypeStorage;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FetchEpisodeDocumentTypeImpl implements FetchEpisodeDocumentType {

	private final EpisodeDocumentTypeStorage episodeDocumentTypeStorage;

	@Override
	public List<EpisodeDocumentTypeBo> run() {
		List<EpisodeDocumentTypeBo> result = episodeDocumentTypeStorage.getEpisodeDocumentType();
		log.debug("Output -> {}", result);
		return result;
	}
}
