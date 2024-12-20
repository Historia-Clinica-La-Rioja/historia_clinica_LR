package net.pladema.medicalconsultation.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiaryOpeningHoursBo extends DiaryOpeningHoursBo {

    private List<UpdateDiaryAppointmentBo> appointments = new ArrayList<>();

    public boolean tryToAdjustAppointment(UpdateDiaryAppointmentBo a) {
        if (this.fitsAppointmentHere(a)) {
            appointments.add(a);
            this.setMyOpeningHoursId(a);
            return true;
        }
        return false;
    }

    private void setMyOpeningHoursId(UpdateDiaryAppointmentBo a) {
        var openingHoursId = this.getOpeningHoursId();
        if (openingHoursId == null) {
            throw new DiaryOpeningHoursException(DiaryOpeningHoursEnumException.OPENING_HOURS_ID_IS_NULL,
                    "Se quiere asignar una franja horaria no almacenada a un turno");
        }
        a.setNewOpeningHoursId(this.getOpeningHoursId());
    }

}
