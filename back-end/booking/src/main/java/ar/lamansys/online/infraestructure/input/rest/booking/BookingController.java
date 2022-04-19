package ar.lamansys.online.infraestructure.input.rest.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.online.application.booking.BookAppointment;
import ar.lamansys.online.application.booking.CancelBooking;
import ar.lamansys.online.application.booking.CheckIfMailExists;
import ar.lamansys.online.domain.booking.BookingAppointmentBo;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.online.domain.booking.BookingPersonBo;
import ar.lamansys.online.infraestructure.input.rest.booking.dto.BookingAppointmentDto;
import ar.lamansys.online.infraestructure.input.rest.booking.dto.BookingDto;
import ar.lamansys.online.infraestructure.input.rest.booking.dto.BookingPersonDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking")
public class BookingController {

	private final BookAppointment bookAppointment;

	private final CheckIfMailExists checkIfMailExists;

	private final CancelBooking cancelBooking;

	private final ObjectMapper objectMapper;

	public BookingController(
			BookAppointment bookAppointment,
			CheckIfMailExists checkIfMailExists,
			CancelBooking cancelBooking
	) {
		this.bookAppointment = bookAppointment;
		this.checkIfMailExists = checkIfMailExists;
		this.cancelBooking = cancelBooking;
		this.objectMapper = new ObjectMapper();
	}

	@PutMapping("/save/preappointment")
	public ResponseEntity<String> bookPreappointment(@RequestBody BookingDto bookingDto) throws JsonProcessingException {
		BookingBo bookingBo = new BookingBo(
				bookingDto.getAppointmentDataEmail(),
				mapToAppointment(bookingDto.getBookingAppointmentDto()),
				mapToPerson(bookingDto.getBookingPersonDto())
		);
		var result = bookAppointment.run(bookingBo);
		log.debug("Saving preappointment => {}", result);
		return ResponseEntity.ok(objectMapper.writeValueAsString(result));
	}

	@PostMapping("/exists/email")
	public ResponseEntity<Boolean> existsEmail(@RequestBody String email) {
		boolean exists = checkIfMailExists.run(email);
		log.debug("Email {} exists : {}", email, exists);
		return ResponseEntity.ok(exists);
	}

	@PutMapping("/cancel")
	public void cancelBooking(@RequestBody String uuid) {
		cancelBooking.run(uuid);
		log.debug("cancel booking {}", uuid);
	}

	private static BookingAppointmentBo mapToAppointment(BookingAppointmentDto bookingAppointmentDto) {
		return new BookingAppointmentBo(
				bookingAppointmentDto.getDiaryId(),
				bookingAppointmentDto.getDay(),
				bookingAppointmentDto.getHour(),
				bookingAppointmentDto.getOpeningHoursId(),
				bookingAppointmentDto.getPhoneNumber(),
				bookingAppointmentDto.getCoverageId(),
				bookingAppointmentDto.getSnomedId(),
				bookingAppointmentDto.getSpecialtyId()
		);
	}

	private static BookingPersonBo mapToPerson(BookingPersonDto bookingPersonDto) {
		if(bookingPersonDto == null) {
			return null;
		}
		return new BookingPersonBo(
				bookingPersonDto.getEmail(),
				bookingPersonDto.getFirstName(),
				bookingPersonDto.getLastName(),
				bookingPersonDto.getIdNumber(),
				bookingPersonDto.getGenderId(),
				bookingPersonDto.getBirthDate()
		);
	}
}
