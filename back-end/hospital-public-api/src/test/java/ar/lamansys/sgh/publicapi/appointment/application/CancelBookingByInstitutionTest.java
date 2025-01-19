package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.CancelBookingByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.exception.CancelBookingAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CancelBookingByInstitutionTest {

	private static final Integer institutionId = 1;
	private static final String uuid = "uuid";

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private CancelBookingByInstitution cancelBookingByInstitution;

	@BeforeEach
	public void setUp() {
		cancelBookingByInstitution = new CancelBookingByInstitution(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun(){
		when(appointmentPublicApiPermissions.canAccessCancelBookingByInstitution(institutionId)).thenReturn(true);

		cancelBookingByInstitution.run(institutionId, uuid);

		verify(bookAppointmentPort).cancelBooking(uuid);
	}

	@Test
	public void cancelAppointmentsAccessDenied(){
		when(appointmentPublicApiPermissions.canAccessCancelBookingByInstitution(institutionId)).thenReturn(false);
		assertThrows(CancelBookingAccessDeniedException.class , () ->
				cancelBookingByInstitution.run(institutionId,uuid));
	}
}
