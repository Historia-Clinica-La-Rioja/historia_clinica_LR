package net.pladema.clinichistory.ips.controller.constraints.validator;

import net.pladema.clinichistory.ips.controller.constraints.AnthropometricDataValid;
import net.pladema.clinichistory.documents.controller.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnthropometricDataValidator implements ConstraintValidator<AnthropometricDataValid, ClinicalObservationDto> {

    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;

    @Override
    public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
        if (clinicalObservationDto != null) {
            int anthropometricValue = Integer.parseInt(clinicalObservationDto.getValue());
            return anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE;
        }
        return true;
    }
}
