package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.lang.annotation.Annotation;
import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

public class DiaryPeriodValidator<A extends Annotation, T extends DiaryADto>
		implements ConstraintValidator<A, T> {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryPeriodValidator.class);

	private final LocalDateMapper localDateMapper;

	public DiaryPeriodValidator(LocalDateMapper localDateMapper) {
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

		return true;
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
