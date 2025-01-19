package ar.lamansys.sgh.publicapi.digitalsignature.application.updatestatusdigitalsignature;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;

import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.DigitalSignatureCallbackStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.MultipleDigitalSignatureCallbackRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateStatusDigitalSignature {

	private final DigitalSignatureCallbackStorage digitalSignatureCallbackStorage;

	public void run(MultipleDigitalSignatureCallbackRequestDto digitalSignatureDocuments) throws DigitalSignatureCallbackException {
		log.debug("Input parameters -> digitalSignatureDocuments {}", digitalSignatureDocuments);
		for (DigitalSignatureCallbackRequestDto digitalSignatureCallbackRequestDto : digitalSignatureDocuments.getDocuments()) {
			digitalSignatureCallbackStorage.updateSignatureStatus(digitalSignatureCallbackRequestDto);
		}
	}
}
