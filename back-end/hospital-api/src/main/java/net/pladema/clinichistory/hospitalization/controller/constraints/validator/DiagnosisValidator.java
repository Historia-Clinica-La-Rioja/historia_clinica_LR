package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.DiagnosisValid;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.DiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiagnosisValidator implements ConstraintValidator<DiagnosisValid, List<DiagnosisDto>> {

    @Override
    public void initialize(DiagnosisValid constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(List<DiagnosisDto> diagnosis, ConstraintValidatorContext context) {

        if (diagnosis == null || diagnosis.isEmpty())
            return true;

        final Set<SnomedDto> set = new HashSet<>();

        for (DiagnosisDto d : diagnosis)
            if (!set.add(d.getSnomed()))
                return false;
        return true;
    }
}
