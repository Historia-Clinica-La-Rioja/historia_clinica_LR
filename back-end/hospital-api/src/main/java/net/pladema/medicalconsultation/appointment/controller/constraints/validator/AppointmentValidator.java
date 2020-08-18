package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AppointmentValidator implements ConstraintValidator<ValidAppointment, CreateAppointmentDto> {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidator.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final DiaryService diaryService;

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final AppointmentService appointmentService;

    private final LocalDateMapper localDateMapper;

    public AppointmentValidator(HealthcareProfessionalService healthcareProfessionalService,
                                DiaryService diaryService,
                                DiaryOpeningHoursService diaryOpeningHoursService, AppointmentService appointmentService,
                                LocalDateMapper localDateMapper){
        super();
        this.healthcareProfessionalService = healthcareProfessionalService;
        this.diaryService = diaryService;
        this.diaryOpeningHoursService = diaryOpeningHoursService;
        this.appointmentService = appointmentService;
        this.localDateMapper = localDateMapper;
    }

    @Override
    public void initialize(ValidAppointment constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(CreateAppointmentDto createAppointmentDto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> createAppointmentDto {}", createAppointmentDto);
        boolean valid = true;

        if(!createAppointmentDto.hasMedicalCoverage()){
            buildResponse(context, "{appointment.new.without.medical.coverage}");
            valid = false;
        }

        DiaryBo diary = diaryService.getDiaryById(createAppointmentDto.getDiaryId());

        if (!diary.isActive()) {
            buildResponse(context, "{appointment.new.diary.inactive}");
            valid = false;
        }
        
        if (diary.isDeleted()) {
            buildResponse(context, "{appointment.new.diary.deleted}");
            valid = false;
        }

        boolean existAppointment = appointmentService.existAppointment(createAppointmentDto.getDiaryId(),
                createAppointmentDto.getOpeningHoursId(),
                localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate()),
                localDateMapper.fromStringToLocalTime(createAppointmentDto.getHour()));

        if (existAppointment) {
            buildResponse(context, "{appointment.overlapping}");
            valid = false;
        }

        if (createAppointmentDto.isOverturn() ) {
            boolean allowNewOverturn = diaryOpeningHoursService.allowNewOverturn(createAppointmentDto.getDiaryId(),
                createAppointmentDto.getOpeningHoursId(), localDateMapper.fromStringToLocalDate(createAppointmentDto.getDate()));
            if (!allowNewOverturn) {
                buildResponse(context, "{appointment.not.allow.new.overturn}");
                valid = false;
            }
        }

        if (UserInfo.hasProfessionalRole() && !diary.isProfessionalAssignShift()) {
            buildResponse(context, "{appointment.new.professional.assign.not.allowed}");
            valid = false;
        }
        Integer professionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
        if (UserInfo.hasProfessionalRole() && !diary.getHealthcareProfessionalId().equals(professionalId)) {
            buildResponse(context, "{appointment.new.professional.id.invalid}");
            valid = false;
        }

        return valid;
    }

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
