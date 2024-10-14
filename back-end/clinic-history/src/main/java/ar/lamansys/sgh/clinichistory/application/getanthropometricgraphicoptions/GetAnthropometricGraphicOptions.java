package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicoptions;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.EAnthropometricGraphicOption;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAnthropometricGraphicOptions {

	private final SharedPatientPort sharedPatientPort;

	public List<EAnthropometricGraphicOption> run (Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);
		List<EAnthropometricGraphicOption> result = EAnthropometricGraphicOption.getAll();
		BasicPatientDto patient = sharedPatientPort.getBasicDataFromPatient(patientId);
		if (patient.getPerson().getPersonAge() == null)
			return Collections.emptyList();
		if (patient.getPerson().getPersonAge().getYears() < 2)
			result.removeIf(option -> option.equals(EAnthropometricGraphicOption.WEIGHT_FOR_HEIGHT));
		log.debug("Output -> result {}", result);
		return result;
	}

}
