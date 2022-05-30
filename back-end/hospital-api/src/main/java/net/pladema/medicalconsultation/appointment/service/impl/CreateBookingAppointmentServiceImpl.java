package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.BookingAppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingAppointment;
import net.pladema.medicalconsultation.appointment.service.booking.CreateBookingAppointmentService;
import org.springframework.stereotype.Service;

@Service
public class CreateBookingAppointmentServiceImpl implements CreateBookingAppointmentService {

    private final BookingAppointmentRepository bookingAppointmentRepository;

    public CreateBookingAppointmentServiceImpl(BookingAppointmentRepository bookingAppointmentRepository) {
        this.bookingAppointmentRepository = bookingAppointmentRepository;
    }

    @Override
    public void execute(BookingAppointmentBo bookingAppointmentBo) {
        this.bookingAppointmentRepository.save(mapTo(bookingAppointmentBo));
    }

    private BookingAppointment mapTo(BookingAppointmentBo bookingAppointmentBo) {
        return new BookingAppointment(bookingAppointmentBo.getAppointmentId(),
                bookingAppointmentBo.getBookingPersonId(),
                bookingAppointmentBo.getUuid());
    }
}
