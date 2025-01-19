package ar.lamansys.online.application.booking;

import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingCannotSendEmailException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookAppointment {

	private final BookingAppointmentStorage bookingAppointmentStorage;

	private final BookingConfirmationMailSender bookingConfirmationMailSender;

	public SavedBookingAppointmentDto run(BookingBo bookingBo) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, BookingCannotSendEmailException {
		SavedBookingAppointmentDto savedBooking = this.bookingAppointmentStorage.save(bookingBo);
		try{
			bookingConfirmationMailSender.sendEmail(bookingBo, savedBooking.getUuid());
		}
		catch (Exception e){
			log.error(e.getMessage(), e);
			this.bookingAppointmentStorage.cancelBooking(savedBooking.getUuid());
			throw new BookingCannotSendEmailException();
		}
		return savedBooking;
	}

}
