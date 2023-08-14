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
	public void run(Integer intermentEpisodeId, Long surgicalReportId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, surgicalReportId {}, reason {}", intermentEpisodeId, surgicalReportId, reason);
		internmentDocumentModificationValidator.execute(intermentEpisodeId, surgicalReportId, reason, EDocumentType.SURGICAL_HOSPITALIZATION_REPORT);
		sharedDocumentPort.deleteDocument(surgicalReportId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(surgicalReportId, reason);
		internmentEpisodeService.deleteEpicrisisDocumentId(intermentEpisodeId);
	}


}
