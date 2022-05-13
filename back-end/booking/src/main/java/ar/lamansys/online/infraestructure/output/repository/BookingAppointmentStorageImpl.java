package ar.lamansys.online.infraestructure.output.repository;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.booking.BookingAppointmentStorage;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

@Service
public class BookingAppointmentStorageImpl implements BookingAppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;

    public BookingAppointmentStorageImpl(SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
    }

    @Override
    public String save(BookingBo bookingBo) {
        return sharedAppointmentPort.saveBooking(
                mapToAppointment(bookingBo),
                mapToBookingPerson(bookingBo),
                bookingBo.getAppointmentDataEmail()
        );
    }

    @Override
    public boolean existsEmail(String email) {
        return sharedAppointmentPort.existsEmail(email);
    }

    @Override
    public void cancelBooking(String email) {
		sharedAppointmentPort.cancelBooking(email);
    }

    @Override
    public Optional<String> getPatientName(String uuid) {
        return sharedAppointmentPort.getPatientName(uuid);
    }

    @Override
    public Optional<String> getProfessionalName(Integer diaryId) {
        return sharedAppointmentPort.getProfessionalName(diaryId);
    }

    private BookingPersonDto mapToBookingPerson(BookingBo bookingBo) {
        if(bookingBo.bookingPerson == null) {
            return null;
        }
        var bookingPerson = bookingBo.bookingPerson;
        return new BookingPersonDto(
                bookingPerson.getBirthDate(),
                bookingPerson.getEmail(),
                bookingPerson.getFirstName(),
                bookingPerson.getGenderId(),
                bookingPerson.getIdNumber(),
                bookingPerson.getLastName()
        );
    }

    private BookingAppointmentDto mapToAppointment(BookingBo bookingBo) {
        var bookingAppointment = bookingBo.bookingAppointment;
        return new BookingAppointmentDto(
                bookingAppointment.getCoverageId(),
                bookingAppointment.getDay(),
                bookingAppointment.getDiaryId(),
                bookingAppointment.getHour(),
                bookingAppointment.getOpeningHoursId(),
                bookingAppointment.getPhoneNumber(),
                bookingAppointment.getSnomedId(),
				bookingAppointment.getSpecialtyId());
    }

}
