package ar.lamansys.sgh.clinichistory.application.getpendingdocumentsbyprofessional;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileSummaryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetDocumentsByProfessional {

	private final DocumentFileStorage documentFileStorage;

	public List<DigitalSignatureDocumentBo> run(Integer userId, Integer institutionId) {
		log.debug("Input parameters -> userId {}, institutionId {}", userId, institutionId);
		List<DigitalSignatureDocumentBo> result = documentFileStorage.findDocumentsByUserAndInstitution(userId, institutionId);
		log.debug("Output result -> {}", result);
		return result;
	}
}
