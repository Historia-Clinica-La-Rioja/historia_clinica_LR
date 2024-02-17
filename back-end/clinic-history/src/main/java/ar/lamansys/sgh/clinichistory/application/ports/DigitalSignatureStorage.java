package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;

public interface DigitalSignatureStorage {

	String generateDigitalSigningLink(DigitalSignatureDataDto dto);

	void updateFile(DocumentFile documentFile);

	DocumentFile getFile(Long id);
}
