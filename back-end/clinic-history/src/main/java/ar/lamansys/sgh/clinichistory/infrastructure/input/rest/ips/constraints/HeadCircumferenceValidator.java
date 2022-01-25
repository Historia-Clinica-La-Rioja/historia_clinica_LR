package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HeadCircumferenceValidator implements ConstraintValidator<HeadCircumferenceDataValid, ClinicalObservationDto> {

	private static final Integer MIN_VALUE = 1;
	private static final Integer MAX_VALUE = 100;

	@Override
	public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
		if (clinicalObservationDto != null && clinicalObservationDto.getValue() != null) {
			try {
				Integer anthropometricValue = Integer.parseInt(clinicalObservationDto.getValue());
				return anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE;
			} catch (NumberFormatException exc) {
				return false;
			}
		}
		return true;
	}
}
