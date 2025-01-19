package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryBookingRestrictionException;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryBookingRestrictionExceptionEnum;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryBookingRestrictionStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateBookingRestriction {

    private final DiaryBookingRestrictionStorage storage;
    private final FeatureFlagsService featureFlagsService;

    public void run(Integer diaryId, LocalDate date) {
        log.debug("Input parameters -> diaryId {}, date {}", diaryId, date);
        if (featureFlagsService.isOn(AppFeature.HABILITAR_RESTRICCION_CANTIDAD_DIAS_ASIG_TURNOS)){
            storage.getByDiaryId(diaryId).ifPresent( restrictionBo -> {
                if (restrictionBo.isValidatedByDays()){
                    validateNumberOfDays(restrictionBo.getDays(),date);
                }
                if (restrictionBo.isValidatedByMonth()){
                    validateCurrentMonth(date);
                }
            });
        }
        log.debug("Output -> {}", Boolean.TRUE);
    }

    private void validateCurrentMonth(LocalDate date) {
        if (!LocalDate.now().getMonth().equals(date.getMonth())) {
            throw new DiaryBookingRestrictionException(
                    DiaryBookingRestrictionExceptionEnum.NOT_CURRENT_MONTH,
                    "No es posible asignar un turno debido a la configuración de la agenda"
            );
        }

    }

    private void validateNumberOfDays(Integer days, LocalDate date) {
        if (Math.abs(ChronoUnit.DAYS.between(date, LocalDate.now())) > days) {
            throw new DiaryBookingRestrictionException(
                    DiaryBookingRestrictionExceptionEnum.NUMBER_OF_DAYS_EXCEEDED,
                    "No es posible asignar un turno debido a la configuración de la agenda"
            );
        }

    }
}
