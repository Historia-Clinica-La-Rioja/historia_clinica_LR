package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

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

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{sisaCode}/appointment")
public class AppointmentPublicController {
	private final SharedBookingPort bookAppointmentPort;
	private final SharedAppointmentPort appointmentPort;
	private final LocalDateMapper localDateMapper;
	public AppointmentPublicController(SharedBookingPort bookAppointmentPort,
									   SharedAppointmentPort appointmentPort,
									   LocalDateMapper localDateMapper) {
		this.bookAppointmentPort = bookAppointmentPort;
		this.appointmentPort = appointmentPort;
		this.localDateMapper = localDateMapper;
	}

	@PostMapping("/booking")
	@ResponseStatus(HttpStatus.CREATED)
	public String bookPreappointment(@RequestBody BookingDto bookingDto) {
		return bookAppointmentPort.makeBooking(bookingDto);
	}


	@PutMapping("/booking/cancel")
	public void cancelBooking(@RequestBody String uuid) {
		bookAppointmentPort.cancelBooking(uuid);
		log.debug("cancel booking {}", uuid);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<PublicAppointmentListDto> getList(
			@PathVariable(name = "sisaCode") String sisaCode,
			@RequestParam(name = "specialtySctid") String specialtySctid,
			@RequestParam(name = "startDate") String startDateStr,
			@RequestParam(name = "endDate") String endDateStr
	) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		appointmentPort.fetchAppointments(sisaCode, specialtySctid, startDate, endDate);
		return Collections.emptyList();
	}




}

/*
/api/public-api/institution/{institutionId}/appointments?specialtyId=Snomed?&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
Method: GET
Params:
specialtyId = id de la especialidad, supongo que snomed para poder mapear?
startDate= fecha de comienzo de la ventana de tiempo
endDate= fecha fin de la ventana de tiempo

 */
