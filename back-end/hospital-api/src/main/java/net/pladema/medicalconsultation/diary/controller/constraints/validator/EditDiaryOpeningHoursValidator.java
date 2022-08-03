package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.constraints.EditDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class EditDiaryOpeningHoursValidator implements ConstraintValidator<EditDiaryOpeningHoursValid, DiaryDto> {

    private static final Logger LOG = LoggerFactory.getLogger(EditDiaryOpeningHoursValidator.class);

    private static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

    private final DiaryMapper diaryMapper;

    @Override
    public void initialize(EditDiaryOpeningHoursValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(DiaryDto diaryDto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryDto {}", diaryDto);

		if(! overturnValuesAreValid(diaryDto, context)) {
			LOG.debug(OUTPUT, Boolean.FALSE);
			return false;
		}

        DiaryBo diaryBo = diaryMapper.toDiaryBo(diaryDto);
        diaryBo.setId(diaryDto.getId());

        Comparator<DiaryOpeningHoursBo> weekDayOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getDayWeekId(), Short::compareTo);
        Comparator<DiaryOpeningHoursBo> timeOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getFrom(), LocalTime::compareTo);

        List<DiaryOpeningHoursBo> openingHours = diaryBo.getDiaryOpeningHours();
        openingHours.sort(weekDayOrder.thenComparing(timeOrder));

        if (diaryBo.getDiaryOpeningHours().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{diary.attention.no-opening-hours}")
                    .addConstraintViolation();
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        boolean overlap = diaryOpeningHoursValidatorService.overlapDiaryOpeningHours(diaryBo, openingHours);

        if (overlap) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overlap}")
                    .addConstraintViolation();
            LOG.debug(OUTPUT, Boolean.FALSE);
            return false;
        }
        LOG.debug(OUTPUT, Boolean.TRUE);
        return true;
    }

	private boolean overturnValuesAreValid(DiaryDto dto, ConstraintValidatorContext context){

		var isInvalidOverturnCount = dto.getDiaryOpeningHours().stream()
				.anyMatch(diaryOpeningHoursDto ->
						diaryOpeningHoursDto.getOverturnCount() != null
						&& diaryOpeningHoursDto.getOverturnCount() < 0
				);
		if(isInvalidOverturnCount) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overturn-value-negative}")
					.addConstraintViolation();
			return false;
		}

		var isInvalidMedicalAttentionType = dto.getDiaryOpeningHours().stream()
				.anyMatch(diaryOpeningHoursDto ->
						diaryOpeningHoursDto.getOverturnCount() != null
								&& diaryOpeningHoursDto.getOverturnCount() > 0
								&& diaryOpeningHoursDto.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS));
		if(isInvalidMedicalAttentionType) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.spontaneous-with-overturn}")
					.addConstraintViolation();
			return false;
		}

		return true;
	}

}
