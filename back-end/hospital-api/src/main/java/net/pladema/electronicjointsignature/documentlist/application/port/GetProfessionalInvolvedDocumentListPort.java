package net.pladema.electronicjointsignature.documentlist.application.port;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import java.util.List;

public interface GetProfessionalInvolvedDocumentListPort {

	List<ElectronicSignatureInvolvedDocumentBo> fetchProfessionalInvolvedDocuments(Integer institutionId, Integer healthcareProfessionalId);

}
