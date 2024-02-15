package net.pladema.clinichistory.hospitalization.application.port;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import java.util.List;

public interface EpisodeDocumentTypeStorage {

	List<EpisodeDocumentTypeBo> getEpisodeDocumentType();

	EpisodeDocumentTypeBo getEpisodeDocumentTypeById(Integer id);
}
