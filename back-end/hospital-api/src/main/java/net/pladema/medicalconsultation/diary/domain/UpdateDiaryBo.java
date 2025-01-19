package net.pladema.medicalconsultation.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiaryBo extends DiaryBo {

    private List<UpdateDiaryOpeningHoursBo> updateDiaryOpeningHours;

    @Override
    public List<DiaryOpeningHoursBo> getDiaryOpeningHours() {
        return updateDiaryOpeningHours.stream()
                .map(updateDiaryOpeningHoursBo -> (DiaryOpeningHoursBo) updateDiaryOpeningHoursBo)
                .collect(Collectors.toList());
    }

    public boolean tryAdjustAppointmentToDiaryOpeningHours(UpdateDiaryAppointmentBo a) {

        if (this.isOutOfDiaryBounds(a)) {
            return false;
        }

        var diaryOpeningHoursBoWhereFits = updateDiaryOpeningHours.stream()
                .filter(updateDiaryOpeningHours -> updateDiaryOpeningHours.tryToAdjustAppointment(a))
                .findFirst();

        // if not present then out of diary
        return diaryOpeningHoursBoWhereFits.isPresent();
    }

}
