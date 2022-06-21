package ar.lamansys.online.application.booking;

import java.util.Optional;

import ar.lamansys.online.domain.booking.BookingBo;

public interface BookingAppointmentStorage {
    String save(BookingBo bookingBo);
    boolean existsEmail(String email);
    void cancelBooking(String email);
    Optional<String> getPatientName(String uuid);
    Optional<String> getProfessionalName(Integer diaryId);
}
