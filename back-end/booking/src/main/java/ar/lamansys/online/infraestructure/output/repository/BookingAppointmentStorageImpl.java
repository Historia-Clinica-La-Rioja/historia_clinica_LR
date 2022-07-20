package ar.lamansys.online.infraestructure.output.repository;

import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.booking.BookingAppointmentStorage;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

@Service
public class BookingAppointmentStorageImpl implements BookingAppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;
	private final SharedInstitutionPort sharedInstitutionPort;

    public BookingAppointmentStorageImpl(SharedAppointmentPort sharedAppointmentPort, SharedInstitutionPort sharedInstitutionPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
		this.sharedInstitutionPort = sharedInstitutionPort;
    }

    @Override
    public SavedBookingAppointmentDto save(BookingBo bookingBo) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException {
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
    public void cancelBooking(String uuid) {
		sharedAppointmentPort.cancelBooking(uuid);
    }

    @Override
    public Optional<String> getPatientName(String uuid) {
        return sharedAppointmentPort.getPatientName(uuid);
    }

    @Override
    public Optional<String> getProfessionalName(Integer diaryId) {
        return sharedAppointmentPort.getProfessionalName(diaryId);
    }

	@Override
	public String getInstitutionAddress(Integer diaryId) {
		Integer institutionId = sharedAppointmentPort.getInstitutionId(diaryId);
		String institutionName = sharedInstitutionPort.fetchInstitutionById(institutionId).getName();
		String institutionAddress = sharedInstitutionPort.fetchInstitutionAddress(institutionId).getCompleteAddress();
		return institutionName + " - " + institutionAddress;
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
                bookingPerson.getLastName(),
				bookingPerson.getPhonePrefix(),
				bookingPerson.getPhoneNumber()
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
                bookingAppointment.getPhonePrefix(),
				bookingAppointment.getPhoneNumber(),
                bookingAppointment.getSnomedId(),
				bookingAppointment.getSpecialtyId());
    }

}
