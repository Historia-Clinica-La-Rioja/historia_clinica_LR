package ar.lamansys.online.application.booking;

import org.springframework.stereotype.Service;

@Service
public class CheckIfMailExists {

    private final BookingAppointmentStorage bookingAppointmentStorage;

    public CheckIfMailExists(BookingAppointmentStorage bookingAppointmentStorage) {
        this.bookingAppointmentStorage = bookingAppointmentStorage;
    }

    public boolean run(String email) {
        return bookingAppointmentStorage.existsEmail(email);
    }
}
