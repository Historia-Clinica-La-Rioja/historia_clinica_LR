package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;

public interface SharedDigitalSignaturePort {

	String generateDigitalSigningLink(DigitalSignatureDataDto dto);
}
