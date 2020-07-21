package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationAuditableEntity;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;

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
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
public class Appointment extends InternationAuditableEntity {

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

}
