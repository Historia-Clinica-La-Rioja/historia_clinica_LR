package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;

@RequiredArgsConstructor
public class DiaryOpeningHoursValidator implements ConstraintValidator<DiaryOpeningHoursValid, DiaryADto> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursValidator.class);

    private static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

    private final DiaryMapper diaryMapper;

    @Override
    public void initialize(DiaryOpeningHoursValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(DiaryADto diaryADto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        DiaryBo diaryBo = diaryMapper.toDiaryBo(diaryADto);

        Comparator<DiaryOpeningHoursBo> weekDayOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getDayWeekId(), Short::compareTo);
        Comparator<DiaryOpeningHoursBo> timeOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getFrom(), LocalTime::compareTo);

        List<DiaryOpeningHoursBo> openingHours = diaryBo.getDiaryOpeningHours();
        openingHours.sort(weekDayOrder.thenComparing(timeOrder));

		if (diaryBo.getCareLines().isEmpty() && openingHoursAllowedProtectedAppointments(openingHours)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.period.invalid.protected-appointments-not-allowed}")
					.addConstraintViolation();
			return false;
		}

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
        return Boolean.TRUE;
    }

	private boolean openingHoursAllowedProtectedAppointments(List<DiaryOpeningHoursBo> openingHours) {
		return openingHours.stream()
				.anyMatch(oh -> oh.getProtectedAppointmentsAllowed());
	}

	private boolean validateProtectedAppointmentsForSpontaneousOpeningHours(List<DiaryOpeningHoursBo> openingHours) {
		return openingHours.stream()
				.anyMatch(oh -> oh.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS) && (oh.getProtectedAppointmentsAllowed() || (oh.getRegulationProtectedAppointmentsAllowed() != null && oh.getRegulationProtectedAppointmentsAllowed())));
	}

	private String overlapDiariesValidationMessage(DiaryBo diaryBo) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		StringBuilder message = new StringBuilder("Verifique los datos ingresados, la agenda que intenta configurar se superpone con la siguiente= Fecha inicio %s - Fecha de fin %s");
		if (diaryBo.getAlias() != null)
			message.append(" - (Alias) ").append(diaryBo.getAlias());
		return String.format(message.toString(), diaryBo.getStartDate().format(dateTimeFormatter), diaryBo.getEndDate().format(dateTimeFormatter));
	}
}
