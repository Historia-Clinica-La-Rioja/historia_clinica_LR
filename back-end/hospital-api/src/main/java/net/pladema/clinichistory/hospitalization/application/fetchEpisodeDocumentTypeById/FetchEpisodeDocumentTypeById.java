package net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentTypeById;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

public interface FetchEpisodeDocumentTypeById {
	EpisodeDocumentTypeBo run(Integer id);
}
