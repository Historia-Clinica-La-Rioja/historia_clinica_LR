package ar.lamansys.online.application.booking;

import ar.lamansys.online.domain.booking.BookingBo;
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

	public String run(BookingBo bookingBo) {
		var uuid = this.bookingAppointmentStorage.save(bookingBo);
		try{
			bookingConfirmationMailSender.sendEmail(bookingBo, uuid);
		}
		catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return uuid;
	}
}
