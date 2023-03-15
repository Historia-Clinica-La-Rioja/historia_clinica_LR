package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidEquipmentAppointmentDiary;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

@RequiredArgsConstructor
public class EquipmentAppointmentDiaryValidator implements ConstraintValidator<ValidEquipmentAppointmentDiary, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentAppointmentDiaryValidator.class);

	private final EquipmentDiaryService equipmentDiaryService;

    @Override
    public void initialize(ValidEquipmentAppointmentDiary constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Integer appointmentId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> appointmentId {}", appointmentId);
        boolean valid = true;

        Optional<EquipmentDiaryBo> diary = equipmentDiaryService.getEquipmentDiaryByAppointment(appointmentId);

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
