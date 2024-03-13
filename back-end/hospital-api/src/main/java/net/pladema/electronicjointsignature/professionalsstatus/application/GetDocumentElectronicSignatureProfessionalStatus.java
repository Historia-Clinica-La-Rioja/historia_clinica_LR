package net.pladema.electronicjointsignature.professionalsstatus.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.professionalsstatus.application.port.DocumentElectronicSignatureProfessionalStatusPort;
import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;

import net.pladema.person.service.PersonService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetDocumentElectronicSignatureProfessionalStatus {

	private DocumentElectronicSignatureProfessionalStatusPort documentElectronicSignatureProfessionalStatusPort;

	private PersonService personService;

	public List<DocumentElectronicSignatureProfessionalStatusBo> run(Long documentId) {
		log.debug("Input parameters -> documentId {}", documentId);
		List<DocumentElectronicSignatureProfessionalStatusBo> result = documentElectronicSignatureProfessionalStatusPort.fetch(documentId);
		fetchAndSetProfessionalsCompleteName(result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void fetchAndSetProfessionalsCompleteName(List<DocumentElectronicSignatureProfessionalStatusBo> result) {
		Set<Integer> personIds = result.stream().map(DocumentElectronicSignatureProfessionalStatusBo::getPersonId).collect(Collectors.toSet());
		Map<Integer, String> personNames = new HashMap<>();
		personIds.forEach(personId -> personNames.put(personId, fetchProfessionalCompleteName(personId)));
		result.forEach(status -> status.setProfessionalCompleteName(personNames.get(status.getPersonId())));
	}

	private String fetchProfessionalCompleteName(Integer personId) {
		return personService.getCompletePersonNameById(personId);
	}

}
