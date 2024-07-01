package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.FetchBookingProfessionalsByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.exception.FetchBookingProfessionalsAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchBookingProfessionalsByInstitutionTest {
	private static final Integer institutionId = 1;
	private static final Integer medicalCoverageId = 1;
	private static final boolean all = true;

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	@InjectMocks
	private FetchBookingProfessionalsByInstitution fetchBookingProfessionalsByInstitution;

	@BeforeEach
	public void setUp() {
		fetchBookingProfessionalsByInstitution = new FetchBookingProfessionalsByInstitution(bookAppointmentPort,appointmentPublicApiPermissions);
	}

	@Test
	public void testRunSuccess(){
		List<BookingProfessionalDto> list =  Collections.singletonList(
				new BookingProfessionalDto(
						1,
						"name",
						true
				));
		when(appointmentPublicApiPermissions.canAccessFetchBookingProfessionals(institutionId)).thenReturn(true);
		when(bookAppointmentPort.fetchBookingProfessionals(institutionId,medicalCoverageId,all)).thenReturn(list);

		List<BookingProfessionalDto> result = fetchBookingProfessionalsByInstitution.run(institutionId,medicalCoverageId,all);

		assertEquals(list,result);
	}

	@Test
	public void fetchBookingProfessionalsAccessDenied() {
		when (appointmentPublicApiPermissions.canAccessFetchBookingProfessionals(institutionId)).thenReturn(false);
		assertThrows(FetchBookingProfessionalsAccessDeniedException.class, () -> {
			fetchBookingProfessionalsByInstitution.run(institutionId,medicalCoverageId,all);
		});
	}
}
