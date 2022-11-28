package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiaryProfessionalId;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import java.util.List;
import java.util.function.Function;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DiaryProfessionalIdValidator implements ConstraintValidator<ValidDiaryProfessionalId, Object[]> {

    private final Logger logger;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;

    private final Function<Integer, Boolean> hasAdministrativeRole;
    private final Function<Integer, Boolean> hasProfessionalRole;

    public DiaryProfessionalIdValidator(HealthcareProfessionalExternalService healthcareProfessionalExternalService, LoggedUserExternalService loggedUserExternalService,
										DiaryAssociatedProfessionalService diaryAssociatedProfessionalService) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO
        );
        this.hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO
        );
		this.diaryAssociatedProfessionalService = diaryAssociatedProfessionalService;
    }

    @Override
    public void initialize(ValidDiaryProfessionalId constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer institutionId = (Integer) parameters[0];
        Integer healthcareProfessionalId = (Integer) parameters[1];
        logger.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
        boolean valid = true;

        if (!hasAdministrativeRole.apply(institutionId)) {

            if (hasProfessionalRole.apply(institutionId)) {
                Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
				List<Integer> associatedHealthcareProfessionals =  diaryAssociatedProfessionalService.getAllAssociatedWithProfessionalsByHealthcareProfessionalId(institutionId, professionalId);
                valid = professionalId.equals(healthcareProfessionalId) || associatedHealthcareProfessionals.contains(healthcareProfessionalId);
            }
        }
        logger.debug("Output -> {}", valid);
        return valid;
    }

}

