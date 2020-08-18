package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiaryProfessionalId;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DiaryProfessionalIdValidator implements ConstraintValidator<ValidDiaryProfessionalId,Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryProfessionalIdValidator.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    @Override
    public void initialize(ValidDiaryProfessionalId constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Integer healthcareProfessionalId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
        boolean valid = true;

        Integer professionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());

        if (UserInfo.hasProfessionalRole() && !professionalId.equals(healthcareProfessionalId))
            valid = false;
        LOG.debug("Output -> {}", valid);
        return valid;
    }

}

