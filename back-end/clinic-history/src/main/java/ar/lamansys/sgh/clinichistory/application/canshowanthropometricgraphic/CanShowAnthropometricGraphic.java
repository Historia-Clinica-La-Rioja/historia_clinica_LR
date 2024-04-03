package ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicEnablementBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CanShowAnthropometricGraphic {

	private final SharedPatientPort sharedPatientPort;
	private final HCEClinicalObservationService hceClinicalObservationService;

	public AnthropometricGraphicEnablementBo run(Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);

		BasicPatientDto basicPatientDto = sharedPatientPort.getBasicDataFromPatient(patientId);
		HCEAnthropometricDataBo anthropometricDataBo = hceClinicalObservationService.getLastAnthropometricDataGeneralState(patientId);

		AnthropometricGraphicEnablementBo result = getAnthropometricGraphicEnablement(basicPatientDto, anthropometricDataBo);

		log.debug("Output -> result {}", result);

		return result;
	}

	private AnthropometricGraphicEnablementBo getAnthropometricGraphicEnablement(BasicPatientDto patient, HCEAnthropometricDataBo anthropometricDataBo){
		AnthropometricGraphicEnablementBo result = new AnthropometricGraphicEnablementBo();
		result.setHasValidAge(true);
		result.setHasValidGender(true);
		boolean hasAnthropometricData = anthropometricDataBo != null &&
				(anthropometricDataBo.getHeight() != null || anthropometricDataBo.getWeight() != null || anthropometricDataBo.getHeadCircumference() != null);
		result.setHasAnthropometricData(hasAnthropometricData);
		if (patient.getPerson().getPersonAge() == null || patient.getPerson().getPersonAge().getYears() > 18)
			result.setHasValidAge(false);
		if (patient.getGender() == null || patient.getGender().getId().equals(EGender.X.getId()))
			result.setHasValidGender(false);
		return result;
	}

}
