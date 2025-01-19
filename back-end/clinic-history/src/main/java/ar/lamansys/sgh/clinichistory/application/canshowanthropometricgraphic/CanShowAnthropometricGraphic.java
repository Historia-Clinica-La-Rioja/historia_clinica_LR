package ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicEnablementBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
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

		if (!this.hasValidAge(basicPatientDto.getPerson()))
			return new AnthropometricGraphicEnablementBo(false);

		AnthropometricGraphicEnablementBo result = new AnthropometricGraphicEnablementBo(true);

		HCEAnthropometricDataBo anthropometricDataBo = hceClinicalObservationService.getLastAnthropometricDataGeneralState(patientId);

		checkAnthropometricData(anthropometricDataBo, result);
		checkValidGender(basicPatientDto, result);

		log.debug("Output -> result {}", result);

		return result;
	}

	private boolean hasValidAge(BasicDataPersonDto person) {
		return (person.getPersonAge() != null && person.getPersonAge().getYears() != null) && person.getPersonAge().getYears() < 19;
	}

	private void checkAnthropometricData(HCEAnthropometricDataBo anthropometricDataBo, AnthropometricGraphicEnablementBo result) {
		boolean hasAnthropometricData = anthropometricDataBo != null &&
				(anthropometricDataBo.getHeight() != null ||
						anthropometricDataBo.getWeight() != null ||
						anthropometricDataBo.getHeadCircumference() != null);
		result.setHasAnthropometricData(hasAnthropometricData);
	}

	private void checkValidGender(BasicPatientDto patient, AnthropometricGraphicEnablementBo result) {
		boolean hasValidGender = patient.getGender() != null
				&& !patient.getGender().getId().equals(EGender.X.getId());
		result.setHasValidGender(hasValidGender);
	}

}
