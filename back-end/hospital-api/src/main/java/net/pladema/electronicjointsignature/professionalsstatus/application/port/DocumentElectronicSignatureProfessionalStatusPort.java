package net.pladema.electronicjointsignature.professionalsstatus.application.port;

import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;

import java.util.List;

public interface DocumentElectronicSignatureProfessionalStatusPort {

	List<DocumentElectronicSignatureProfessionalStatusBo> fetchDocumentInvolvedProfessionalStatus(Long documentId);

}
