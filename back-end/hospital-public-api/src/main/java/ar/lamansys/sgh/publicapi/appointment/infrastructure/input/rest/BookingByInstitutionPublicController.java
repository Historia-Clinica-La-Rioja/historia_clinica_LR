package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;


import java.time.LocalDate;
import java.util.Collection;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingbyinstitution.FetchBookingByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.makeBooking.MakeBooking;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingCannotSendEmailException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking")
@Tag(name = "PublicApi Turnos", description = "Booking by institution")
@RestController
public class BookingByInstitutionPublicController {

	private final LocalDateMapper localDateMapper;

	private final FetchBookingByInstitution fetchBookingByInstitution;
	private final MakeBooking makeBooking;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SavedBookingAppointmentDto bookPreappointment(@PathVariable (name = "institutionId") Integer institutionId, @RequestBody BookingDto bookingDto) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		return makeBooking.run(institutionId,bookingDto);
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
		return fetchBookingByInstitution.run(institutionId,identificationNumber,startDate,endDate);
	}

}
