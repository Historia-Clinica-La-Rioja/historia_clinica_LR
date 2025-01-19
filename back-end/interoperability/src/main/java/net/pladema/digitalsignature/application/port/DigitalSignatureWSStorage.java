package net.pladema.digitalsignature.application.port;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;

public interface DigitalSignatureWSStorage {

	String generateDigitalSigningLink(DigitalSignatureDataDto dto);
}
