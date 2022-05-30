package net.pladema.clinichistory.hospitalization.service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;

public interface InternmentDocumentModificationValidator {

	void execute(Integer intermentEpisodeId, Long documentId, String reason, EDocumentType documentType);
}
