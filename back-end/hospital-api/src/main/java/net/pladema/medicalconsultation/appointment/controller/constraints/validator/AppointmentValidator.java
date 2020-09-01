package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.service.HealthcareProfessionalService;


@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
public class AppointmentValidator implements ConstraintValidator<ValidAppointment, Object[]> {

    private static final int UTC_DIFFERENCE = 3;

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidator.class);

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final DiaryService diaryService;

    private final DiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

    private final AppointmentService appointmentService;

    private final LocalDateMapper localDateMapper;

    private final LoggedUserExternalService loggedUserExternalService;

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
		return validAppoinment(context, createAppointmentDto) 
				&& validDiary(context, diary)
				&& validRole(context, institutionId, diary);
	}

	private boolean validAppoinment(ConstraintValidatorContext context, CreateAppointmentDto createAppointmentDto) {
		boolean valid = true;
		
		if(beforeNow(createAppointmentDto)){
            buildResponse(context, "{appointment.new.beforeToday}");
            valid = false;
        }
        
        if(!createAppointmentDto.hasMedicalCoverage()){
            buildResponse(context, "{appointment.new.without.medical.coverage}");
            valid = false;
        }
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
                List.of(ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO));

        if (!hasAdministrativeRole) {
            boolean hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                    List.of(ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO));

            if (hasProfessionalRole && !diary.isProfessionalAssignShift()) {
                buildResponse(context, "{appointment.new.professional.assign.not.allowed}");
                valid = false;
            }
            Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
            if (hasProfessionalRole && !diary.getHealthcareProfessionalId().equals(professionalId)) {
                buildResponse(context, "{appointment.new.professional.id.invalid}");
                valid = false;
            }
        }
		return valid;
	}

	private boolean beforeNow(CreateAppointmentDto createAppointmentDto) {
		LocalDate apmtDate = localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate());
		LocalTime apmtTime = localDateMapper.fromStringToLocalTime(createAppointmentDto.getHour());
		return apmtDate.isBefore(LocalDate.now())
				|| (apmtDate.equals(LocalDate.now()) && apmtTime.isBefore(LocalTime.now().minusHours(UTC_DIFFERENCE)));
	}

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
