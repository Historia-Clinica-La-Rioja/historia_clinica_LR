package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;
import net.pladema.sgx.auditable.entity.SGXAuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends SGXAuditableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_type_id", nullable = false)
    private LocalDate dateTypeId;

    @Column(name = "hour", nullable = false)
    private LocalTime hour;

    @Column(name = "appointment_state_id", nullable = false)
    private Short appointmentStateId;

    @Column(name = "is_overturn", columnDefinition = "boolean default false", nullable = false)
    private Boolean isOverturn;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "health_insurance_id")
    private Integer healthInsuranceId;

    @Column(name = "medical_coverage_name", length = 150)
    private String medicalCoverageName;

    @Column(name = "medical_coverage_affiliate_number", nullable = false)
    private String medicalCoverageAffiliateNumber;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    public static Appointment newFromAppointmentBo(AppointmentBo appointmentBo) {
        Appointment result = new Appointment();
        result.setDateTypeId(appointmentBo.getDate());
        result.setHour(appointmentBo.getHour());
        result.setIsOverturn(appointmentBo.isOverturn());
        result.setPatientId(appointmentBo.getPatientId());
        result.setAppointmentStateId(AppointmentState.ASSIGNED);

        String medicalCoverage = appointmentBo.getMedicalCoverageName();
        if(medicalCoverage == null && appointmentBo.getHealthInsuranceId() == null )
            medicalCoverage = "Sumar";
        result.setMedicalCoverageName(medicalCoverage);

        result.setMedicalCoverageAffiliateNumber(appointmentBo.getMedicalCoverageAffiliateNumber());
        result.setHealthInsuranceId(appointmentBo.getHealthInsuranceId());
        result.setPhoneNumber(appointmentBo.getPhoneNumber());
        return result;

    }
}
