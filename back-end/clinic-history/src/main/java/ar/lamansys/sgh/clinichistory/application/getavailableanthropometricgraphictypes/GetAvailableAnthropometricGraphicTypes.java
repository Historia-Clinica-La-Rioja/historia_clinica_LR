package ar.lamansys.sgh.clinichistory.application.getavailableanthropometricgraphictypes;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GetAvailableAnthropometricGraphicTypes {

	private final static List<EAnthropometricGraphicType> ONLY_EVOLUTION = List.of(EAnthropometricGraphicType.EVOLUTION);
	private final static List<EAnthropometricGraphicType> ONLY_PERCENTILES = List.of(EAnthropometricGraphicType.PERCENTILES);
	private final static List<EAnthropometricGraphicType> PERCENTILES_AND_ZSCORE = List.of(EAnthropometricGraphicType.PERCENTILES, EAnthropometricGraphicType.ZSCORE);

	private final SharedPatientPort sharedPatientPort;

	public List<EAnthropometricGraphicType> run (Integer patientId, EAnthropometricGraphic graphic){
		log.debug("Input parameters -> patientId {}, graphic {}", patientId, graphic);
		List<EAnthropometricGraphicType> result = getAvailableGraphicTypes(patientId, graphic);
		log.debug("Output -> result {}", result);
		return result;
	}

	private List<EAnthropometricGraphicType> getAvailableGraphicTypes(Integer patientId, EAnthropometricGraphic graphic){
		BasicPatientDto basicPatientDto = sharedPatientPort.getBasicDataFromPatient(patientId);
		
		if (basicPatientDto.getPerson().getPersonAge() == null || basicPatientDto.getPerson().getPersonAge().getYears() > 18)
			return Collections.emptyList();

		boolean withoutGender = basicPatientDto.getPerson().getGenderId() == null || basicPatientDto.getPerson().getGenderId().equals(EGender.X.getId());

		if (graphic.equals(EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE) || graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_AGE))
			return withoutGender ? ONLY_EVOLUTION : PERCENTILES_AND_ZSCORE;
		else
			return withoutGender ? ONLY_EVOLUTION : ONLY_PERCENTILES;
	}

}
