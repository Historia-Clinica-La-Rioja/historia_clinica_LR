package net.pladema.electronicjointsignature.signdocument.application.port;

import java.util.List;

public interface SignDocumentElectronicJointSignaturePort {

	void changeDocumentSignatureStatusToSigned(Integer healthcareProfessionalId, List<Long> documentIds);

	List<Short> fetchDocumentsSignatureStatus(Integer healthcareProfessionalId, List<Long> documentIds);

}
