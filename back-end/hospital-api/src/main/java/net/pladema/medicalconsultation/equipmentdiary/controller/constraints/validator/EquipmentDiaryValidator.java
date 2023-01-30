package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ValidEquipmentDiary;

import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryRepository;

import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

@RequiredArgsConstructor
public class EquipmentDiaryValidator implements ConstraintValidator<ValidEquipmentDiary,Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryValidator.class);

    private final EquipmentDiaryRepository equipmentDiaryRepository;

    @Override
    public void initialize(ValidEquipmentDiary constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Integer equipmentDiaryId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> equipmentDiaryId {}", equipmentDiaryId);
        boolean valid = true;

        Optional<EquipmentDiary> equipmentDiary = equipmentDiaryRepository.findById(equipmentDiaryId);

        if (!equipmentDiary.isPresent()){
            buildResponse(context, "{diary.invalid.id}");
            valid = false;
        }
        else if (equipmentDiary.get().isDeleted()){
            buildResponse(context, "{diary.deleted}");
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

