package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingAppointmentBo {
    private final Integer bookingPersonId;
    private final Integer appointmentId;
    private final String uuid;

}
