package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.HealthHistoryConditionValid;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HealthHistoryConditionDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HealthHistoryConditionValidator implements ConstraintValidator<HealthHistoryConditionValid, List<HealthHistoryConditionDto>> {

    @Override
    public void initialize(HealthHistoryConditionValid constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(List<HealthHistoryConditionDto> healthHistoryConditionDtoList, ConstraintValidatorContext context) {

        if (healthHistoryConditionDtoList == null || healthHistoryConditionDtoList.isEmpty())
            return true;

        final Set<SnomedDto> set = new HashSet<>();

        for (HealthHistoryConditionDto healthHistoryConditionDto : healthHistoryConditionDtoList)
            if (!set.add(healthHistoryConditionDto.getSnomed()))
                return false;
        return true;
    }
}
