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

    private final String healthInsuranceName;

    private final String medicalCoverageName;

    private final String medicalCoverageAffiliateNumber;

    private final String appointmentState;

    public AttentionTypeReportItemBo(DailyAppointmentVo dailyAppointmentVo){
        this.hour = dailyAppointmentVo.getHour();
        this.patientId = dailyAppointmentVo.getPatientId();
        this.healthInsuranceName = dailyAppointmentVo.getHealthInsuranceName();
        this.medicalCoverageName = dailyAppointmentVo.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = dailyAppointmentVo.getMedicalCoverageAffiliateNumber();
        this.appointmentState = dailyAppointmentVo.getAppointmentState();
    }
}
