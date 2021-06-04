package net.pladema.clinichistory.ips.controller.constraints.validator;

import net.pladema.clinichistory.ips.controller.constraints.HeightDataValid;
import net.pladema.clinichistory.documents.controller.dto.ClinicalObservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HeightDataValidator implements ConstraintValidator<HeightDataValid, ClinicalObservationDto> {

    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;

    @Override
    public boolean isValid(ClinicalObservationDto clinicalObservationDto, ConstraintValidatorContext context){
        if (clinicalObservationDto != null && clinicalObservationDto.getValue() != null) {
            try {
                int anthropometricValue = Integer.parseInt(clinicalObservationDto.getValue());
                return anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE;
            } catch (NumberFormatException exc) {
                return false;
            }
        }
        return true;
    }
}
