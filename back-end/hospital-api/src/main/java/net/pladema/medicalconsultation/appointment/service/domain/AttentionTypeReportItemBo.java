package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AttentionTypeReportItemBo {

    private final LocalTime hour;

    private final Integer patientId;

    private final Integer patientMedicalCoverageId;

    private final String appointmentState;

    public AttentionTypeReportItemBo(DailyAppointmentVo dailyAppointmentVo){
        this.hour = dailyAppointmentVo.getHour();
        this.patientId = dailyAppointmentVo.getPatientId();
        this.patientMedicalCoverageId = dailyAppointmentVo.getPatientMedicalCoverageId();
        this.appointmentState = dailyAppointmentVo.getAppointmentState();
    }
}
