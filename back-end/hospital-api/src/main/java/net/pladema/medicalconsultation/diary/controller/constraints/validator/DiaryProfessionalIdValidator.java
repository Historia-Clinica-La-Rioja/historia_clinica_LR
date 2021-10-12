package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiaryProfessionalId;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.List;

@RequiredArgsConstructor
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DiaryProfessionalIdValidator implements ConstraintValidator<ValidDiaryProfessionalId, Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryProfessionalIdValidator.class);

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final LoggedUserExternalService loggedUserExternalService;

    @Override
    public void initialize(ValidDiaryProfessionalId constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer institutionId = (Integer) parameters[0];
        Integer healthcareProfessionalId = (Integer) parameters[1];
        LOG.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
        boolean valid = true;

        boolean hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                List.of(ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO));

        if (!hasAdministrativeRole) {
            boolean hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                    List.of(ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO));

            if (hasProfessionalRole) {
                Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
                valid = professionalId.equals(healthcareProfessionalId);
            }
        }
        LOG.debug("Output -> {}", valid);
        return valid;
    }

}

