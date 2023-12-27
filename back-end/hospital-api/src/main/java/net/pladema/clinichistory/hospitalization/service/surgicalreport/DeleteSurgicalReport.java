package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteSurgicalReport {

	private final InternmentEpisodeService internmentEpisodeService;
	private final InternmentDocumentModificationValidator internmentDocumentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;

	@Transactional
	public void run(Integer intermentEpisodeId, Long documentId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, documentId {}, reason {}", intermentEpisodeId, documentId, reason);
		internmentDocumentModificationValidator.execute(intermentEpisodeId, documentId, reason, EDocumentType.SURGICAL_HOSPITALIZATION_REPORT);
		sharedDocumentPort.deleteDocument(documentId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(documentId, reason);
		internmentEpisodeService.deleteEpicrisisDocumentId(intermentEpisodeId);
	}


}
