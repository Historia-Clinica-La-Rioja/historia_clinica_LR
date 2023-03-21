package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractEquipmentDiaryPeriodValidator<A extends Annotation, T extends EquipmentDiaryADto>
		implements ConstraintValidator<A, T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractEquipmentDiaryPeriodValidator.class);

	private final LocalDateMapper localDateMapper;

	public AbstractEquipmentDiaryPeriodValidator(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	@Override
	public void initialize(A constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(T diary, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> equipmentDiaryADto {}", diary);
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
