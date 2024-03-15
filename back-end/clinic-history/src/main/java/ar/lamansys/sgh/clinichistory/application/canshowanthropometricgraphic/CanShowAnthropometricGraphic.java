package ar.lamansys.sgh.clinichistory.application.canshowanthropometricgraphic;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
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

	public Boolean run(Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);

		BasicPatientDto basicPatientDto = sharedPatientPort.getBasicDataFromPatient(patientId);
		HCEAnthropometricDataBo anthropometricDataBo = hceClinicalObservationService.getLastAnthropometricDataGeneralState(patientId);

		Boolean result = canShowGraphic(basicPatientDto, anthropometricDataBo);

		log.debug("Output -> result {}", result);

		return result;
	}

	private Boolean canShowGraphic(BasicPatientDto patient, HCEAnthropometricDataBo anthropometricDataBo){
		boolean hasAnthropometricData = anthropometricDataBo != null &&
				(anthropometricDataBo.getHeight() != null || anthropometricDataBo.getWeight() != null || anthropometricDataBo.getHeadCircumference() != null);
		if (patient.getPerson().getPersonAge() == null || patient.getPerson().getPersonAge().getYears() > 18)
			return Boolean.FALSE;
		if ((patient.getPerson().getGenderId() == null || patient.getPerson().getGenderId().equals(EGender.X.getId()))
				&& !hasAnthropometricData)
			return Boolean.FALSE;
		return Boolean.TRUE;
	}

}
