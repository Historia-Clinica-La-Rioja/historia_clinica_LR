package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.HealthHistoryConditionValid;
import net.pladema.internation.controller.ips.dto.HealthHistoryConditionDto;
import net.pladema.internation.controller.ips.dto.SnomedDto;

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
    public boolean isValid(List<HealthHistoryConditionDto> healthHistoryConditionDtos, ConstraintValidatorContext context) {

        if (healthHistoryConditionDtos == null || healthHistoryConditionDtos.isEmpty())
            return true;

        final Set<SnomedDto> set = new HashSet<>();

        for (HealthHistoryConditionDto healthHistoryConditionDto : healthHistoryConditionDtos)
            if (!set.add(healthHistoryConditionDto.getSnomed()))
                return false;
        return true;
    }
}
