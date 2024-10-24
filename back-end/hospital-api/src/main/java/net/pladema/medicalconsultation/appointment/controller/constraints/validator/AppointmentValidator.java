package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
public class AppointmentValidator implements ConstraintValidator<ValidAppointment, Object[]> {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidator.class);

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final DiaryService diaryService;

    private final DiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

    private final AppointmentService appointmentService;

    private final LocalDateMapper localDateMapper;

    private final LoggedUserExternalService loggedUserExternalService;

	private final static Integer NO_INSTITUTION = -1;


	@Value("${test.stress.disable.validation:false}")
	private boolean disableValidation;

    @Override
    public void initialize(ValidAppointment constraintAnnotation) {
        // nothing to do
    }

	@Override
	public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
		Integer institutionId = (Integer) parameters[0];
		CreateAppointmentDto createAppointmentDto = (CreateAppointmentDto) parameters[1];
		LOG.debug("Input parameters -> institutionId {}, createAppointmentDto {}", institutionId, createAppointmentDto);

		DiaryBo diary = diaryService.getDiaryById(createAppointmentDto.getDiaryId());
		return validAppointment(context, createAppointmentDto)
				&& validDiary(context, diary)
				&& validRole(context, institutionId, diary);
	}

	private boolean validAppointment(ConstraintValidatorContext context, CreateAppointmentDto createAppointmentDto) {
		boolean valid = true;

        if (!createAppointmentDto.isOverturn()) {
        	boolean existAppointment = appointmentService.existAppointment(createAppointmentDto.getDiaryId(),
        			createAppointmentDto.getOpeningHoursId(),
        			localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate()),
        			localDateMapper.fromStringToLocalTime(createAppointmentDto.getHour()));
        	
        	if (existAppointment) {
        		buildResponse(context, "{appointment.overlapping}");
        		valid = false;
        	}
        }
        
        if (createAppointmentDto.isOverturn()) {
        	boolean allowNewOverturn = diaryOpeningHoursValidatorService.allowNewOverturn(createAppointmentDto.getDiaryId(),
        			createAppointmentDto.getOpeningHoursId(), localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate()));
        	if (!allowNewOverturn) {
        		buildResponse(context, "{appointment.not.allow.new.overturn}");
        		valid = false;
        	}
        }
        return valid;
	}

	private boolean validDiary(ConstraintValidatorContext context, DiaryBo diary) {
		boolean valid = true;
		if (!diary.isActive()) {
            buildResponse(context, "{appointment.new.diary.inactive}");
            valid = false;
        }
        
        if (diary.isDeleted()) {
            buildResponse(context, "{appointment.new.diary.deleted}");
            valid = false;
        }
		return valid;
	}

	private boolean validRole(ConstraintValidatorContext context, Integer institutionId,
			DiaryBo diary) {
		boolean valid = true;
		boolean hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO);

		boolean hasManagerRole = loggedUserExternalService.hasAnyRoleInstitution(NO_INSTITUTION,
				ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_REGIONAL, ERole.GESTOR_DE_ACCESO_LOCAL);

        if (!hasAdministrativeRole && !hasManagerRole) {
            boolean hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                    ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO);

			Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());

            if (hasProfessionalRole &&
					((diary.getHealthcareProfessionalId().equals(professionalId) || diary.getDiaryAssociatedProfessionalsId().contains(professionalId))
							&& !diary.isProfessionalAssignShift())) {
                buildResponse(context, "{appointment.new.professional.assign.not.allowed}");
                valid = false;
            }
        }
		return valid;
	}

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
