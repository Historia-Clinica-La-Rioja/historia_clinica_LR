package net.pladema.emergencycare.application.updateemergencycareevolutionnote;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.createemergencycareevolutionnote.CreateEmergencyCareEvolutionNote;
import net.pladema.emergencycare.service.EmergencyCareEpisodeAdministrativeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteDocumentService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@AllArgsConstructor
@Service
public class UpdateEmergencyCareEvolutionNote {

	private final SharedDocumentPort sharedDocumentPort;

	private final CreateEmergencyCareEvolutionNote createEmergencyCareEvolutionNote;

	private final EmergencyCareEvolutionNoteDocumentService emergencyCareEvolutionNoteDocumentService;

	private final EmergencyCareEpisodeAdministrativeDischargeService emergencyCareEpisodeAdministrativeDischargeService;

	@Transactional
	public EmergencyCareEvolutionNoteBo run (Integer institutionId, Integer episodeId, Long oldDocumentId, EmergencyCareEvolutionNoteDocumentBo evolutionNoteDocument) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, oldDocumentId {}, evolutionNoteDocument {}", institutionId, episodeId, oldDocumentId, evolutionNoteDocument);
		assertValidEpisode(episodeId);
		sharedDocumentPort.deleteDocument(oldDocumentId, DocumentStatus.ERROR);
		emergencyCareEvolutionNoteDocumentService.deleteByDocumentId(oldDocumentId);
		EmergencyCareEvolutionNoteBo result = createEmergencyCareEvolutionNote.run(institutionId, episodeId, evolutionNoteDocument);
		Long initialDocumentId = sharedDocumentPort.getInitialDocumentId(oldDocumentId).orElse(oldDocumentId);
		sharedDocumentPort.updateInitialDocumentId(result.getDocumentId(), initialDocumentId);
		log.debug("Output -> result {}", result);
		return result;
	}

	private void assertValidEpisode(Integer episodeId){
		Assert.isTrue(!emergencyCareEpisodeAdministrativeDischargeService.hasAdministrativeDischarge(episodeId), "No se puede modificar la nota de evolución porque el episodio ya cuenta con alta administrativa");
	}

}
