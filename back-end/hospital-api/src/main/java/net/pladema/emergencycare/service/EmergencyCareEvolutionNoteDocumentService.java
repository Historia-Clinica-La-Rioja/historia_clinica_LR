package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import java.util.List;
import java.util.Optional;

public interface EmergencyCareEvolutionNoteDocumentService {

	List<EmergencyCareEvolutionNoteDocumentBo> getAllDocumentsByEpisodeId(Integer episodeId);

	Optional<EmergencyCareEvolutionNoteDocumentBo> getByDocumentId(Long documentId);

}
