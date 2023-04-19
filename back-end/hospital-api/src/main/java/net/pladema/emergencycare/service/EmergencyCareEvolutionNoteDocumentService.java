package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import java.util.List;

public interface EmergencyCareEvolutionNoteDocumentService {

	List<EmergencyCareEvolutionNoteDocumentBo> getAllDocumentsByEpisodeId(Integer episodeId);

}
