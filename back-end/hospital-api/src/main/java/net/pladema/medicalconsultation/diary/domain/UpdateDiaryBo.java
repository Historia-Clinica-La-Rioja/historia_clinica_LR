package net.pladema.medicalconsultation.diary.domain;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiaryBo extends DiaryBo {

    private static final Consumer<OpeningHoursBo> THROW_EXCEPTION_OPENING_HOURS_OVERTURN_CONSUMER = openingHoursBo -> {
        throw new OverturnsLimitException(
                "Se encuentran asignados una cantidad mayor de sobreturnos al límite establecido en la franja del dia " +
                        EDayOfWeek.map(openingHoursBo.getDayWeekId()).getDescription() +
                        ", en el horario de " + openingHoursBo.getFrom() + "hs. a " + openingHoursBo.getTo() + "hs.");
    };

    private List<UpdateDiaryOpeningHoursBo> updateDiaryOpeningHours;

    @Override
    public List<DiaryOpeningHoursBo> getDiaryOpeningHours() {
        return updateDiaryOpeningHours.stream()
                .map(updateDiaryOpeningHoursBo -> (DiaryOpeningHoursBo) updateDiaryOpeningHoursBo)
                .collect(Collectors.toList());
    }

    public boolean tryAdjustAppointmentToDiaryOpeningHours(UpdateDiaryAppointmentBo a) {

        if (this.isOutOfDiaryBounds(a)) {
            // out of diary
            return false;
        }
        var diaryOpeningHoursBoWhereFits = updateDiaryOpeningHours.stream()
                .filter(updateDiaryOpeningHours -> updateDiaryOpeningHours.tryToAdjustAppointment(a))
                .findFirst();

        diaryOpeningHoursBoWhereFits
                .filter(UpdateDiaryOpeningHoursBo::isOverturnsOutOfLimit)
                .map(UpdateDiaryOpeningHoursBo::getOpeningHours)
                .ifPresent(THROW_EXCEPTION_OPENING_HOURS_OVERTURN_CONSUMER);

        // if not present then out of diary
        return diaryOpeningHoursBoWhereFits.isPresent();
    }

}
