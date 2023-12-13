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
import java.time.format.DateTimeFormatter;
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

		if (validateProtectedAppointmentsForSpontaneousOpeningHours(openingHours)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.period.invalid.protected-appointments-not-allowed-in-spontaneous-attention}")
					.addConstraintViolation();
			return false;
		}

		if (diaryBo.getDiaryOpeningHours().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{diary.attention.no-opening-hours}")
                    .addConstraintViolation();
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

		boolean overlapBetweenDiaryOpeningHours = diaryOpeningHoursValidatorService.overlapBetweenDiaryOpeningHours(openingHours);

		if (overlapBetweenDiaryOpeningHours) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overlap}")
					.addConstraintViolation();
			LOG.debug(OUTPUT, Boolean.FALSE);
			return false;
		}

		if (diaryBo.getDiaryAssociatedProfessionalsId().isEmpty()) {
			List<DiaryBo> diariesOverlaps = diaryOpeningHoursValidatorService.overlapWithOthersDiaries(diaryBo, openingHours);
			if (!diariesOverlaps.isEmpty()) {
				diariesOverlaps.forEach( dov -> {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(overlapDiariesValidationMessage(dov))
							.addConstraintViolation();
				});
				LOG.debug(OUTPUT, Boolean.FALSE);
				return false;
			}
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

	private boolean validateProtectedAppointmentsForSpontaneousOpeningHours(List<DiaryOpeningHoursBo> openingHours) {
		return openingHours.stream()
				.anyMatch(oh -> oh.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS) &&
						((oh.getProtectedAppointmentsAllowed() != null && oh.getProtectedAppointmentsAllowed()) || (oh.getRegulationProtectedAppointmentsAllowed() != null && oh.getRegulationProtectedAppointmentsAllowed())));
	}

	private String overlapDiariesValidationMessage(DiaryBo diaryBo) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		StringBuilder message = new StringBuilder("Verifique los datos ingresados, la agenda que intenta configurar se superpone con la siguiente - Fecha inicio %s - Fecha de fin %s");
		if (diaryBo.getAlias() != null)
			message.append(" - Alias ").append(diaryBo.getAlias());
		return String.format(message.toString(), diaryBo.getStartDate().format(dateTimeFormatter), diaryBo.getEndDate().format(dateTimeFormatter));
	}

}
