package net.pladema.electronicjointsignature.documentlist.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.documentlist.application.port.GetProfessionalInvolvedDocumentListPort;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import net.pladema.person.service.PersonService;

import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetProfessionalInvolvedDocumentList {

	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private GetProfessionalInvolvedDocumentListPort getProfessionalInvolvedDocumentListPort;

	private PersonService personService;

	public Page<ElectronicSignatureInvolvedDocumentBo> run(ElectronicSignatureDocumentListFilterBo filter, Pageable pageable) {
		log.debug("Input parameters -> filter {}, pageable {}", filter, pageable);
		setFilterHealthcareProfessionalId(filter);
		Page<ElectronicSignatureInvolvedDocumentBo> result = getProfessionalInvolvedDocumentListPort.fetchProfessionalInvolvedDocuments(filter, pageable);
		fetchAndSetPersonsName(result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void setFilterHealthcareProfessionalId(ElectronicSignatureDocumentListFilterBo filter) {
		Integer healthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		filter.setHealthcareProfessionalId(healthcareProfessionalId);
	}

	private void fetchAndSetPersonsName(Page<ElectronicSignatureInvolvedDocumentBo> result) {
		List<Integer> patientPersonIds = result.stream().map(ElectronicSignatureInvolvedDocumentBo::getPatientPersonId).collect(Collectors.toList());
		HashMap<Integer, String> patientCompleteNames = getPersonCompleteNames(patientPersonIds);
		List<Integer> professionalPersonIds = result.stream().map(ElectronicSignatureInvolvedDocumentBo::getResponsibleHealthcareProfessionalPersonId).collect(Collectors.toList());
		HashMap<Integer, String> professionalCompleteNames = getPersonCompleteNames(professionalPersonIds);
		result.forEach(element -> setPersonsName(element, patientCompleteNames, professionalCompleteNames));
	}

	private HashMap<Integer, String> getPersonCompleteNames(List<Integer> personIds) {
		HashMap<Integer, String> result = new HashMap<>();
		Set<Integer> patientIds = new HashSet<>(personIds);
		patientIds.forEach(personId -> result.put(personId, fetchPersonCompleteName(personId)));
		return result;
	}

	private void setPersonsName(ElectronicSignatureInvolvedDocumentBo element, HashMap<Integer, String> patientCompleteNames, HashMap<Integer, String> professionalCompleteNames) {
		element.setPatientCompleteName(patientCompleteNames.get(element.getPatientPersonId()));
		element.setResponsibleProfessionalCompleteName(professionalCompleteNames.get(element.getResponsibleHealthcareProfessionalPersonId()));
	}

	private String fetchPersonCompleteName(Integer personId) {
		return personService.getCompletePersonNameById(personId).toUpperCase();
	}

}
