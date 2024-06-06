package ar.lamansys.online.application.booking;

import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookAppointment {

	private final BookingAppointmentStorage bookingAppointmentStorage;

	private final BookingConfirmationMailSender bookingConfirmationMailSender;

	public BookAppointment(BookingAppointmentStorage bookingAppointmentStorage, BookingConfirmationMailSender bookingConfirmationMailSender) {
		this.bookingAppointmentStorage = bookingAppointmentStorage;
		this.bookingConfirmationMailSender = bookingConfirmationMailSender;
	}

	public SavedBookingAppointmentDto run(BookingBo bookingBo) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException {
		SavedBookingAppointmentDto savedBooking = this.bookingAppointmentStorage.save(bookingBo);
		try{
			bookingConfirmationMailSender.sendEmail(bookingBo, savedBooking.getUuid());
		}
		catch (Exception e){
			log.error(e.getMessage(), e);
			this.bookingAppointmentStorage.cancelBooking(savedBooking.getUuid());
		}
		return savedBooking;
	}

}
