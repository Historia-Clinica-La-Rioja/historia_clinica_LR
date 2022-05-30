package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "booking_appointment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingAppointment {
    @EmbeddedId
    private BookingAppointmentPK pk;

    public BookingAppointment(Integer appointmentId, Integer bookingPersonId, String uuid){
        this.pk = new BookingAppointmentPK(appointmentId, bookingPersonId, uuid);
    }
}
