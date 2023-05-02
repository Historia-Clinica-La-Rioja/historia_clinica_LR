package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidEquipmentAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import java.time.*;
import java.util.Optional;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
public class EquipmentAppointmentValidator implements ConstraintValidator<ValidEquipmentAppointment, Object[]> {

	private static final Logger LOG = LoggerFactory.getLogger(EquipmentAppointmentValidator.class);

    private final EquipmentDiaryService equipmentDiaryService;

    private final EquipmentDiaryOpeningHoursValidatorService equipmentDiaryOpeningHoursValidatorService;

    private final AppointmentService appointmentService;

    private final LocalDateMapper localDateMapper;

    private final LoggedUserExternalService loggedUserExternalService;

    private final InstitutionExternalService institutionExternalService;

	@Value("${test.stress.disable.validation:false}")
	private boolean disableValidation;

    @Override
    public void initialize(ValidEquipmentAppointment constraintAnnotation) {
        // nothing to do
    }

	@Override
	public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
		Integer institutionId = (Integer) parameters[0];
		CreateAppointmentDto createAppointmentDto = (CreateAppointmentDto) parameters[3];
		LOG.debug("Input parameters -> institutionId {}, createAppointmentDto {}", institutionId, createAppointmentDto);

		ZoneId timezone = institutionExternalService.getTimezone(institutionId);

		Optional<CompleteEquipmentDiaryBo> equipmentDiary = equipmentDiaryService.getEquipmentDiary(createAppointmentDto.getDiaryId());
		return validAppoinment(context, createAppointmentDto, timezone)
				&& validDiary(context, equipmentDiary.get())
				&& validRole(context, institutionId, equipmentDiary.get());
	}

	private boolean validAppoinment(ConstraintValidatorContext context, CreateAppointmentDto createAppointmentDto, ZoneId timezone) {
		boolean valid = true;
		
		if(beforeNow(createAppointmentDto, timezone) && !disableValidation){
            buildResponse(context, "{appointment.new.beforeToday}");
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
        	boolean allowNewOverturn = equipmentDiaryOpeningHoursValidatorService.allowNewOverturn(createAppointmentDto.getDiaryId(),
        			createAppointmentDto.getOpeningHoursId(), localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate()));
        	if (!allowNewOverturn) {
        		buildResponse(context, "{appointment.not.allow.new.overturn}");
        		valid = false;
        	}
        }
        return valid;
	}

	private boolean validDiary(ConstraintValidatorContext context, CompleteEquipmentDiaryBo equipmentDiaryBo) {
		boolean valid = true;
		if (!equipmentDiaryBo.isActive()) {
            buildResponse(context, "{appointment.new.diary.inactive}");
            valid = false;
        }
        
        if (equipmentDiaryBo.isDeleted()) {
            buildResponse(context, "{appointment.new.diary.deleted}");
            valid = false;
        }
		return valid;
	}

	private boolean validRole(ConstraintValidatorContext context, Integer institutionId,
							  CompleteEquipmentDiaryBo equipmentDiaryBo) {
		boolean valid = true;
		boolean hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
                ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO,ERole.ADMINISTRATIVO_RED_DE_IMAGENES);

        return valid;
	}

	private boolean beforeNow(CreateAppointmentDto createAppointmentDto, ZoneId timezone) {
		LocalDate apmtDate = localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate());
		LocalTime apmtTime = localDateMapper.fromStringToLocalTime(createAppointmentDto.getHour());

		ZonedDateTime apmtDateTime = LocalDateTime.of(apmtDate, apmtTime)
				.atZone(timezone);

		ZonedDateTime nowInTimezone = LocalDateTime.now()
				.atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
				.withZoneSameInstant(timezone);
		return apmtDateTime.isBefore(nowInTimezone);
	}

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
