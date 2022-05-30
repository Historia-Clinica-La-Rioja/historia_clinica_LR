package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

public interface SharedBookingPort {

    String saveBooking(BookingAppointmentDto bookingAppointmentDto, BookingPersonDto bookingPersonDto, String email);

    boolean existsEmail(String email);

    void cancelBooking(String email);

    Optional<String> getPatientName(String uuid);

    Optional<String> getProfessionalName(Integer diaryId);
}
