package net.pladema.medicalconsultation.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiaryOpeningHoursBo extends DiaryOpeningHoursBo {

    private List<UpdateDiaryAppointmentBo> appointments;

    public boolean isOverturnsOutOfLimit() {
        Map<LocalDate, Long> overturnsByDate = appointments.stream()
                .filter(UpdateDiaryAppointmentBo::isOverturn)
                .collect(groupingBy(UpdateDiaryAppointmentBo::getDate, counting()));
        return overturnsByDate.values()
                .stream()
                .anyMatch(overturns -> overturns > this.getOverturnCount().intValue());
    }

    public boolean tryToAdjustAppointment(UpdateDiaryAppointmentBo a) {
        if (this.belongsTo(a)) {
            appointments.add(a);
            return true;
        }
        return false;
    }

}
