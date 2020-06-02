package net.pladema.featureflags.controller.constraints.validators;

import net.pladema.featureflags.controller.constraints.SGHNotNull;
import net.pladema.featureflags.service.FeatureFlagsService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SGHNotNullValidator implements ConstraintValidator<SGHNotNull, Object> {


    private List<String> featureFlags = new ArrayList<>();

    private final FeatureFlagsService featureFlagsService;

    public SGHNotNullValidator(FeatureFlagsService featureFlagsService) {
        this.featureFlagsService = featureFlagsService;
    }

    @Override
    public void initialize(SGHNotNull constraintAnnotation) {
        if (constraintAnnotation.ffs() != null)
            featureFlags = Arrays.asList(constraintAnnotation.ffs());
    }

    @Override
    public boolean isValid(Object attribute, ConstraintValidatorContext context) {
        for (String ff : featureFlags)
            if (featureFlagsService.isOn(ff))
                return attribute != null;
        return true;
    }
}