package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentDiary;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

@RequiredArgsConstructor
public class AppointmentDiaryValidator implements ConstraintValidator<ValidAppointmentDiary, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentDiaryValidator.class);

    private final DiaryService diaryService;


    @Override
    public void initialize(ValidAppointmentDiary constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Integer appointmentId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> appointmentId {}", appointmentId);
        boolean valid = true;

        Optional<DiaryBo> diary = diaryService.getDiaryByAppointment(appointmentId);

        if (!diary.isPresent() || diary.get().isDeleted()){
            buildResponse(context, "{appointment.new.diary.deleted}");
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
