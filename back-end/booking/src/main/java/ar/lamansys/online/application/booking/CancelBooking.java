package ar.lamansys.online.application.booking;

import org.springframework.stereotype.Service;

@Service
public class CancelBooking {

    private final BookingAppointmentStorage bookingAppointmentStorage;

    public CancelBooking(BookingAppointmentStorage bookingAppointmentStorage) {
        this.bookingAppointmentStorage = bookingAppointmentStorage;
    }

    public void run(String uuid) {
        bookingAppointmentStorage.cancelBooking(uuid);
    }
}
