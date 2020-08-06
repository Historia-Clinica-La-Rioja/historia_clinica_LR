package net.pladema.medicalconsultation.appointment.service.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
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

    private final Integer openingHoursId;

    public AppointmentBo(AppointmentDiaryVo appointmentDiaryVo) {
        super();
        this.id = appointmentDiaryVo.getId();
        this.diaryId = appointmentDiaryVo.getDiaryId();
        this.patientId = appointmentDiaryVo.getPatientId();
        this.date = appointmentDiaryVo.getDate();
        this.hour = appointmentDiaryVo.getHour();
        this.appointmentStateId = appointmentDiaryVo.getAppointmentStateId();
        this.overturn = appointmentDiaryVo.isOverturn();
        this.openingHoursId = null;
    }

    public static AppointmentBo newFromAppointment(Appointment appointment) {
        return new AppointmentBo(appointment.getId(),
                null,
                appointment.getPatientId(),
                appointment.getDateTypeId(),
                appointment.getHour(),
                appointment.getAppointmentStateId(),
                appointment.getIsOverturn(),
                null);
    }
}
