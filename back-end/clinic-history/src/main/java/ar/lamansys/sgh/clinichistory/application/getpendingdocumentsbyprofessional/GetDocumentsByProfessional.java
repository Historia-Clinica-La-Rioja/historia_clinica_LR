package ar.lamansys.sgh.clinichistory.application.getpendingdocumentsbyprofessional;

import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetDocumentsByProfessional {

	private final DocumentFileStorage documentFileStorage;

	public Page<DigitalSignatureDocumentBo> run(Integer userId, Integer institutionId, Pageable pageable) {
		log.debug("Input parameters -> userId {}, institutionId {}, pageable {}", userId, institutionId, pageable);
		Page<DigitalSignatureDocumentBo> result = documentFileStorage.findDocumentsByUserAndInstitution(userId, institutionId, pageable);
		log.debug("Output result -> {}", result);
		return result;
	}

}
