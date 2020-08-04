package net.pladema.medicalconsultation.appointment.service.domain;


import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@ToString
@AllArgsConstructor
public class AppointmentBo {

    private final Integer id;

    private final Integer diaryId;

    private final Integer patientId;

    private final LocalDate date;

    private final LocalTime hour;

    private final Short appointmentStateId;

    private final boolean overturn;

    public AppointmentBo(AppointmentDiaryVo appointmentDiaryVo) {
        super();
        this.id = appointmentDiaryVo.getId();
        this.diaryId = appointmentDiaryVo.getDiaryId();
        this.patientId = appointmentDiaryVo.getPatientId();
        this.date = appointmentDiaryVo.getDate();
        this.hour = appointmentDiaryVo.getHour();
        this.appointmentStateId = appointmentDiaryVo.getAppointmentStateId();
        this.overturn = appointmentDiaryVo.isOverturn();
    }
}
