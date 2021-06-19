package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeightDataValidator implements ConstraintValidator<WeightDataValid, ClinicalObservationDto> {

    private static final Double MIN_VALUE = 0.0;
    private static final Double MAX_VALUE = 1000.0;

    @Override
    public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
        if (clinicalObservationDto != null && clinicalObservationDto.getValue() != null) {
            try {
                double anthropometricValue = Double.parseDouble(clinicalObservationDto.getValue());
                return anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE;
            } catch (NumberFormatException exc) {
                return false;
            }
        }
        return true;
    }
}