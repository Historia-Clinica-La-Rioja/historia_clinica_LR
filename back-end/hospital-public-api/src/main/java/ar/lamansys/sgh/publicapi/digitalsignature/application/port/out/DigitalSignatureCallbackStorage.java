package ar.lamansys.sgh.publicapi.digitalsignature.application.port.out;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;

public interface DigitalSignatureCallbackStorage {

	void updateSignatureStatus(DigitalSignatureCallbackRequestDto dto) throws DigitalSignatureCallbackException;
}
