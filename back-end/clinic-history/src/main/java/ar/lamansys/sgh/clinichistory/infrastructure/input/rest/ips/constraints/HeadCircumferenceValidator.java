package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HeadCircumferenceValidator implements ConstraintValidator<HeadCircumferenceDataValid, ClinicalObservationDto> {

	private static final Float MIN_VALUE = 1f;
	private static final Float MAX_VALUE = 100f;

	@Override
	public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
		if (clinicalObservationDto != null && clinicalObservationDto.getValue() != null) {
			try {
				Float anthropometricValue = Float.parseFloat(clinicalObservationDto.getValue());
				return anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE;
			} catch (NumberFormatException exc) {
				return false;
			}
		}
		return true;
	}
}
