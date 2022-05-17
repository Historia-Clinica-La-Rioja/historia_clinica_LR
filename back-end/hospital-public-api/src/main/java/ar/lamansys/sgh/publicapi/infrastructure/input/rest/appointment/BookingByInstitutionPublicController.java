package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking")
public class BookingByInstitutionPublicController {
	private final SharedBookingPort bookAppointmentPort;
	private final SharedAppointmentPort appointmentPort;
	private final LocalDateMapper localDateMapper;

	public BookingByInstitutionPublicController(SharedBookingPort bookAppointmentPort,
												SharedAppointmentPort appointmentPort,
												LocalDateMapper localDateMapper) {
		this.bookAppointmentPort = bookAppointmentPort;
		this.appointmentPort = appointmentPort;
		this.localDateMapper = localDateMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String bookPreappointment(@RequestBody BookingDto bookingDto) {
		return bookAppointmentPort.makeBooking(bookingDto);
	}


	@PutMapping("/cancel")
	public void cancelBooking(@RequestBody String uuid) {
		bookAppointmentPort.cancelBooking(uuid);
		log.debug("cancel booking {}", uuid);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<PublicAppointmentListDto> getBookingList(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "identificationNumber", required = false) String identificationNumber,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr
	) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		return appointmentPort.fetchAppointments(institutionId, identificationNumber, List.of((short)6), startDate, endDate);
	}

}
