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
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBo {

    private  Integer id;

    private  Integer diaryId;

    private  Integer patientId;

    private  LocalDate date;

    private  LocalTime hour;

    private  Short appointmentStateId;

    private  boolean overturn;

    private Integer openingHoursId;

    String medicalCoverageName;

    String medicalCoverageAffiliateNumber;

    Integer healthInsuranceId;

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
        this.medicalCoverageName = null;
        this.medicalCoverageAffiliateNumber = null;
        this.healthInsuranceId = null;
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
                appointment.getHealthInsuranceId());
    }
}
