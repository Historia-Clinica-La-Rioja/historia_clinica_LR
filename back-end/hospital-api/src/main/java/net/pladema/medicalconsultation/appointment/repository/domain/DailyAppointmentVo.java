package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import java.time.LocalTime;

@Getter
@ToString
public class DailyAppointmentVo {

    private final LocalTime hour;

    private final Integer patientId;

    private final String appointmentState;

    private final LocalTime openingHourFrom;

    private final LocalTime openingHourTo;

    private final Integer patientMedicalCoverageId;

    private final Short medicalAttentionTypeId;

    public DailyAppointmentVo(Appointment appointment,
                              AppointmentState appointmentState,
                              LocalTime openingHourFrom,
                              LocalTime openingHourTo,
                              Short medicalAttentionTypeId
    ){
        this.hour = appointment.getHour();
        this.patientId = appointment.getPatientId();
        this.patientMedicalCoverageId = appointment.getPatientMedicalCoverageId();
        this.appointmentState = appointmentState.getDescription();
        this.openingHourFrom = openingHourFrom;
        this.openingHourTo = openingHourTo;
        this.medicalAttentionTypeId = medicalAttentionTypeId;
    }

}
