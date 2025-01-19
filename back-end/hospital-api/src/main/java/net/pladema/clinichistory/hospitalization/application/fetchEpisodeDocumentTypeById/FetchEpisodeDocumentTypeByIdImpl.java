package net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentTypeById;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentTypeStorage;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FetchEpisodeDocumentTypeByIdImpl implements FetchEpisodeDocumentTypeById {

	private final EpisodeDocumentTypeStorage episodeDocumentTypeStorage;

	@Override
	public EpisodeDocumentTypeBo run(Integer id) {
		log.debug("Input parameters -> id {} ", id);
		EpisodeDocumentTypeBo result = episodeDocumentTypeStorage.getEpisodeDocumentTypeById(id);
		log.debug("Output -> {} ", result);
		return result;
	}
}
