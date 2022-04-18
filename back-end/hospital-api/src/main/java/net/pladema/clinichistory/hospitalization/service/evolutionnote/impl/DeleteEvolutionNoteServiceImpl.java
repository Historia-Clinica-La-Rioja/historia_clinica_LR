package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.DeleteEvolutionNoteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteEvolutionNoteServiceImpl implements DeleteEvolutionNoteService {

	private final InternmentEpisodeService internmentEpisodeService;
	private final SharedDocumentPort sharedDocumentPort;

	@Override
	@Transactional
	public void execute(Integer intermentEpisodeId, Long evolutionNoteId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, evolutionNoteId {}, reason {}",
				intermentEpisodeId, evolutionNoteId, reason);
		sharedDocumentPort.deleteDocument(evolutionNoteId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(evolutionNoteId, reason);
	}
}
