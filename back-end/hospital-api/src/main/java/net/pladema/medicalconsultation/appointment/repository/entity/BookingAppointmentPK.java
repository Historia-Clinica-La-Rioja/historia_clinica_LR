package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookingAppointmentPK implements Serializable {
    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "booking_person_id", nullable = false)
    private Integer bookingPersonId;

    @Column(name = "uuid", nullable = false)
    private String uuid;
}
