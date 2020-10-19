package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.AnthropometricDataValid;
import net.pladema.clinichistory.ips.controller.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnthropometricDataValidator implements ConstraintValidator<AnthropometricDataValid, ClinicalObservationDto> {

    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;

    @Override
    public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
        if (clinicalObservationDto != null) {
            int weightValue = Integer.parseInt(clinicalObservationDto.getValue());
            return weightValue >= MIN_VALUE && weightValue <= MAX_VALUE;
        }
        return true;
    }
}
