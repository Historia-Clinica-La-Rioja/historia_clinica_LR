package net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentType;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import java.util.List;

public interface FetchEpisodeDocumentType {

	List<EpisodeDocumentTypeBo> run();
}
