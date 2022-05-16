package ar.lamansys.online.infraestructure.input.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.booking.BookAppointment;
import ar.lamansys.online.application.booking.CancelBooking;
import ar.lamansys.online.application.booking.CheckIfMailExists;
import ar.lamansys.online.application.insurance.FetchHealthcareInsurances;
import ar.lamansys.online.application.integration.FetchBookingInstitutions;
import ar.lamansys.online.domain.booking.BookingAppointmentBo;
import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.online.domain.booking.BookingPersonBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BookingExternalService implements SharedBookingPort {
	private final BookAppointment bookAppointment;
	private final CancelBooking cancelBooking;
	private final FetchBookingInstitutions fetchBookingInstitutions;

	private final FetchHealthcareInsurances fetchHealthcareInsurances;
	public BookingExternalService(
			BookAppointment bookAppointment,
			CheckIfMailExists checkIfMailExists,
			CancelBooking cancelBooking,
			FetchBookingInstitutions fetchBookingInstitutions,
			FetchHealthcareInsurances fetchHealthcareInsurances) {
		this.bookAppointment = bookAppointment;
		this.cancelBooking = cancelBooking;
		this.fetchBookingInstitutions = fetchBookingInstitutions;
		this.fetchHealthcareInsurances = fetchHealthcareInsurances;
	}

	public String makeBooking(BookingDto bookingDto) {
		BookingBo bookingBo = new BookingBo(
				bookingDto.getAppointmentDataEmail(),
				mapToAppointment(bookingDto.getBookingAppointmentDto()),
				mapToPerson(bookingDto.getBookingPersonDto())
		);
		return bookAppointment.run(bookingBo);
	}

	public void cancelBooking(String uuid) {
		cancelBooking.run(uuid);
		log.debug("cancel booking {}", uuid);
	}

	@Override
	public List<BookingInstitutionDto> fetchAllBookingInstitutions() {
		return fetchBookingInstitutions.run().stream()
				.map(institution -> new BookingInstitutionDto(institution.getId(), institution.getDescription()))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingHealthInsuranceDto> fetchAllMedicalCoverages() {
		List<BookingHealthInsuranceDto> result = fetchHealthcareInsurances.run().stream()
				.map(insurance -> new BookingHealthInsuranceDto(insurance.getId(), insurance.getName()))
				.collect(Collectors.toList());
		log.debug("Get all booking institutions => {}", result);
		return result;
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
