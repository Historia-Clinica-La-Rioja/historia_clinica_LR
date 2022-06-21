package net.pladema.medicalconsultation.appointment.service.booking;

import net.pladema.medicalconsultation.appointment.repository.domain.BookingAppointmentBo;

public interface CreateBookingAppointmentService {
    void execute(BookingAppointmentBo bookingAppointmentBo);
}
