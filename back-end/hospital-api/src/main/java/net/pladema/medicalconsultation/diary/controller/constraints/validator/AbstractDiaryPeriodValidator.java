package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public abstract class AbstractDiaryPeriodValidator<A extends Annotation, T extends DiaryADto>
		implements ConstraintValidator<A, T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDiaryPeriodValidator.class);

	private final LocalDateMapper localDateMapper;

	public AbstractDiaryPeriodValidator(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	@Override
	public void initialize(A constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(T diary, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> diaryADto {}", diary);
		LocalDate startDate = localDateMapper.fromStringToLocalDate(diary.getStartDate());
		LocalDate endDate = localDateMapper.fromStringToLocalDate(diary.getEndDate());

		if (endDate.isBefore(startDate)) {
			buildResponse(context, "{diary.period.invalid.range}");
			return false;
		}


		List<Integer> overlappingDiary = getOverlappingDiary(diary, startDate, endDate);

		if (!overlappingDiary.isEmpty()) {
			buildResponse(context, "{diary.period.invalid.overlap}");
			return false;
		}
		return true;
	}

	protected abstract List<Integer> getOverlappingDiary(T diary,LocalDate startDate, LocalDate endDate);

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
