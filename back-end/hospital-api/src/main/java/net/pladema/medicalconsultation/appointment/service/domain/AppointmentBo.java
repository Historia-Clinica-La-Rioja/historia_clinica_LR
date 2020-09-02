package net.pladema.medicalconsultation.appointment.service.domain;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBo {

    private Integer id;

    private Integer diaryId;

    private Integer patientId;

    private LocalDate date;

    private LocalTime hour;

    private Short appointmentStateId;

    private boolean overturn;

    private Integer openingHoursId;

    private String medicalCoverageName;

    private String medicalCoverageAffiliateNumber;

    private Integer healthInsuranceId;

    private Short medicalAttentionTypeId;
    
    private String stateChangeReason;

    private String phoneNumber;

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
        this.medicalCoverageName = appointmentDiaryVo.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = appointmentDiaryVo.getMedicalCoverageAffiliateNumber();
        this.healthInsuranceId = appointmentDiaryVo.getHealthInsuranceId();
        this.medicalAttentionTypeId = appointmentDiaryVo.getMedicalAttentionTypeId();
    }
    
    public AppointmentBo(AppointmentVo appointmentVo) {
        super();
        this.id = appointmentVo.getId();
        this.patientId = appointmentVo.getPatientId();
        this.date = appointmentVo.getDate();
        this.hour = appointmentVo.getHour();
        this.appointmentStateId = appointmentVo.getAppointmentStateId();
        this.overturn = appointmentVo.isOverturn();
        this.openingHoursId = null;
        this.medicalCoverageName = appointmentVo.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = appointmentVo.getMedicalCoverageAffiliateNumber();
        this.healthInsuranceId = appointmentVo.getHealthInsuranceId();
        this.medicalAttentionTypeId = appointmentVo.getMedicalAttentionTypeId();
        this.stateChangeReason = appointmentVo.getStateChangeReason();
        this.diaryId = appointmentVo.getDiaryId();
    }

    public static AppointmentBo newFromAppointment(Appointment appointment) {
        return new AppointmentBo(appointment.getId(),
                null,
                appointment.getPatientId(),
                appointment.getDateTypeId(),
                appointment.getHour(),
                appointment.getAppointmentStateId(),
                appointment.getIsOverturn(),
                null,
                appointment.getMedicalCoverageName(),
                appointment.getMedicalCoverageAffiliateNumber(),
                appointment.getHealthInsuranceId(),
                null,
                null,
                appointment.getPhoneNumber());
    }
}
