package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
public class AppointmentAssnPK implements Serializable {

    @Column(name = "diary_id", nullable = false)
    private Integer diaryId;

    @Column(name = "opening_hours_id", nullable = false)
    private Integer openingHoursId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;
}
