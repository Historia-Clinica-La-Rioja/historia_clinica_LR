package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.DeleteEpicrisisService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteEpicrisisServiceImpl implements DeleteEpicrisisService {

	private final InternmentEpisodeService internmentEpisodeService;
	private final SharedDocumentPort sharedDocumentPort;

	@Override
	@Transactional
	public void execute(Integer intermentEpisodeId, Long epicrisisId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, epicrisisId {}, reason {}",
				intermentEpisodeId, epicrisisId, reason);
		sharedDocumentPort.deleteDocument(epicrisisId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(epicrisisId, reason);
		internmentEpisodeService.deleteEpicrisisDocumentId(intermentEpisodeId);
	}

}
