package ar.lamansys.sgh.publicapi.digitalsignature.infrastructure.output;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackEnumException;
import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.DigitalSignatureCallbackStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DigitalSignatureCallbackStorageImpl implements DigitalSignatureCallbackStorage {

	private final SharedDocumentPort sharedDocumentPort;

	@Override
	public void updateSignatureStatus(DigitalSignatureCallbackRequestDto digitalSignature) throws DigitalSignatureCallbackException {
		log.debug("Input parameter -> digitalSignature {}", digitalSignature);
		assertEqualHash(digitalSignature.getHash(), digitalSignature.getDocumentId());
		sharedDocumentPort.updateSignatureStatus(digitalSignature);
		log.debug("No output");
	}

	private void assertEqualHash(String hash, Long documentId) throws DigitalSignatureCallbackException {
		log.debug("Input parameter -> hash {}, documentId {}", hash, documentId);
		String savedHash = sharedDocumentPort.getDigitalSignatureHashById(documentId);
		if(hash!=null && !(savedHash.equals(hash)))
			throw new DigitalSignatureCallbackException(DigitalSignatureCallbackEnumException.HASH_NOT_MATCH, String.format("El hash %s no pertenece al documento con id %s", hash, documentId));
	}
}
