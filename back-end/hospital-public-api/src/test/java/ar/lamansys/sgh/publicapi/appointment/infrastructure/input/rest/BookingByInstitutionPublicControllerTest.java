package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingbyinstitution.FetchBookingByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.makeBooking.MakeBooking;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingCannotSendEmailException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingExceptionEnum;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingByInstitutionPublicControllerTest {

	private static final Integer institutionId = 1;

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private FetchBookingByInstitution fetchBookingByInstitution;

	@Mock
	private MakeBooking makeBooking;

	private BookingByInstitutionPublicController bookingByInstitutionPublicController;

	@BeforeEach
	public void setUp() {
		bookingByInstitutionPublicController = new BookingByInstitutionPublicController(localDateMapper,fetchBookingByInstitution,makeBooking);
	}

	@Test
	void testBookPreappointment() throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		Integer institutionId = 1;
		BookingDto bookingDto = new BookingDto();
		SavedBookingAppointmentDto expectedSavedBookingAppointmentDto = new SavedBookingAppointmentDto(1,1,"uuid");

		when(makeBooking.run(institutionId, bookingDto)).thenReturn(expectedSavedBookingAppointmentDto);

		SavedBookingAppointmentDto actualSavedBookingAppointmentDto = bookingByInstitutionPublicController.bookPreappointment(institutionId, bookingDto);

		assertEquals(expectedSavedBookingAppointmentDto, actualSavedBookingAppointmentDto);
	}

	@Test
	void testBookPreappointmentThrowsProfessionalAlreadyBookedException() throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		Integer institutionId = 1;
		BookingDto bookingDto = new BookingDto();

		doThrow(new ProfessionalAlreadyBookedException()).when(makeBooking).run(institutionId, bookingDto);

		assertThrows(ProfessionalAlreadyBookedException.class, () -> {
			bookingByInstitutionPublicController.bookPreappointment(institutionId, bookingDto);
		});
	}

	@Test
	void testBookPreappointmentThrowsBookingPersonMailNotExistsException() throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		Integer institutionId = 1;
		BookingDto bookingDto = new BookingDto();

		doThrow(new BookingPersonMailNotExistsException()).when(makeBooking).run(institutionId, bookingDto);

		assertThrows(BookingPersonMailNotExistsException.class, () -> {
			bookingByInstitutionPublicController.bookPreappointment(institutionId, bookingDto);
		});
	}

	@Test
	void testBookPreappointmentThrowsSaveExternalBookingException() throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException, BookingCannotSendEmailException {
		Integer institutionId = 1;
		BookingDto bookingDto = new BookingDto();

		SaveExternalBookingExceptionEnum code = SaveExternalBookingExceptionEnum.OPENING_HOURS_DOES_NOT_ALLOW_EXTERNAL_APPOINTMENTS;
		SaveExternalBookingException exception = new SaveExternalBookingException(code, "message");

		doThrow(exception).when(makeBooking).run(institutionId, bookingDto);

		assertThrows(SaveExternalBookingException.class, () -> {
			bookingByInstitutionPublicController.bookPreappointment(institutionId, bookingDto);
		});
	}

	@Test
	void testGetBookingList(){
		List<PublicAppointmentListDto> list = Collections.emptyList();
		when(localDateMapper.fromStringToLocalDate("2024-07-01")).thenReturn(LocalDate.of(2024, 7, 1));
		when(localDateMapper.fromStringToLocalDate("2024-07-31")).thenReturn(LocalDate.of(2024, 7, 31));
		when(fetchBookingByInstitution.run(institutionId,"123456",LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31))).thenReturn(list);

		Collection<PublicAppointmentListDto> result = bookingByInstitutionPublicController.getBookingList(institutionId,"123456","2024-07-01", "2024-07-31");

		assertEquals(list, result);
	}

}
