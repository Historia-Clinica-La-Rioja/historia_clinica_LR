package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiary;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class DiaryValidator implements ConstraintValidator<ValidDiary,Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryValidator.class);

    private final DiaryRepository diaryRepository;

    @Override
    public void initialize(ValidDiary constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Integer diaryId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryId {}", diaryId);
        boolean valid = true;

        Optional<Diary> diary = diaryRepository.findById(diaryId);

        if (!diary.isPresent()){
            buildResponse(context, "{diary.invalid.id}");
            valid = false;
        }
        else if (diary.get().isDeleted()){
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

