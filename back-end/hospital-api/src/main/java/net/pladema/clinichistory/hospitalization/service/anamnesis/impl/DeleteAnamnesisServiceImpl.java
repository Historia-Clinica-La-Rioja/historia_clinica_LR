package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.DeleteAnamnesisService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteAnamnesisServiceImpl implements DeleteAnamnesisService {

	private final InternmentEpisodeService internmentEpisodeService;
	private final SharedDocumentPort sharedDocumentPort;

	@Override
	@Transactional
	public void execute(Integer intermentEpisodeId, Long anamnesisId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, anamnesisId {}, reason {}",
				intermentEpisodeId, anamnesisId, reason);
		sharedDocumentPort.deleteDocument(anamnesisId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(anamnesisId, reason);
		internmentEpisodeService.deleteAnamnesisDocumentId(intermentEpisodeId);
	}

}
