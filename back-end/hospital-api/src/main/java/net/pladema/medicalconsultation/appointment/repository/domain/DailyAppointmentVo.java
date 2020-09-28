package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.person.repository.entity.HealthInsurance;

import java.time.LocalTime;

@Getter
@ToString
public class DailyAppointmentVo {

    private final LocalTime hour;

    private final Integer patientId;

    private final String healthInsuranceName;

    private final String medicalCoverageName;

    private final String medicalCoverageAffiliateNumber;

    private final String appointmentState;

    private final LocalTime openingHourFrom;

    private final LocalTime openingHourTo;

    private final Short medicalAttentionTypeId;

    public DailyAppointmentVo(Appointment appointment,
                              HealthInsurance healthInsurance,
                              AppointmentState appointmentState,
                              LocalTime openingHourFrom,
                              LocalTime openingHourTo,
                              Short medicalAttentionTypeId
    ){
        this.hour = appointment.getHour();
        this.patientId = appointment.getPatientId();
        this.medicalCoverageName = appointment.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = appointment.getMedicalCoverageAffiliateNumber();
        this.appointmentState = appointmentState.getDescription();
        this.openingHourFrom = openingHourFrom;
        this.openingHourTo = openingHourTo;
        this.medicalAttentionTypeId = medicalAttentionTypeId;

        if (healthInsurance != null)
            this.healthInsuranceName = healthInsurance.getName();
        else
            this.healthInsuranceName = null;
    }

}
