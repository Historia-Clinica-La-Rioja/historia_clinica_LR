package net.pladema.electronicjointsignature.healthcareprofessionallicense.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.healthcareprofessionallicense.application.port.ElectronicJointSignatureInstitutionalProfessionalLicenseListPort;
import net.pladema.electronicjointsignature.healthcareprofessionallicense.domain.ElectronicJointSignatureInstitutionProfessionalBo;

import net.pladema.person.service.PersonService;

import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchProfessionalLicenseData {

	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private ElectronicJointSignatureInstitutionalProfessionalLicenseListPort electronicJointSignatureInstitutionalProfessionalLicenseListPort;

	private PersonService personService;

	public List<ElectronicJointSignatureInstitutionProfessionalBo> run(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		Integer currentUserHealthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		List<ElectronicJointSignatureInstitutionProfessionalBo> result = fetchInstitutionProfessionals(institutionId, currentUserHealthcareProfessionalId);
		log.debug("Output -> {}", result);
		return result;
	}

	private List<ElectronicJointSignatureInstitutionProfessionalBo> fetchInstitutionProfessionals(Integer institutionId, Integer healthcareProfessionalId) {
		List<ElectronicJointSignatureInstitutionProfessionalBo> result = electronicJointSignatureInstitutionalProfessionalLicenseListPort.fetchInstitutionProfessional(institutionId, healthcareProfessionalId);
		result.forEach(this::fetchProfessionalCompleteName);
		return result;
	}

	public void fetchProfessionalCompleteName(ElectronicJointSignatureInstitutionProfessionalBo professional) {
		String completeName = personService.getCompletePersonNameById(professional.getPersonId());
		professional.setCompleteName(completeName);
	}

}
