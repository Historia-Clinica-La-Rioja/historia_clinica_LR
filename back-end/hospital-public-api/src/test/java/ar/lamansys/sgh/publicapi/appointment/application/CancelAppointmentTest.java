package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.CancelAppointment;
import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.exception.CancelAppointmentAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CancelAppointmentTest {

	private static final Integer institutionId = 1;
	private static final Integer appointmentId = 1;
	private static final String reason = "reason";

	@Mock
	private SharedAppointmentPort appointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private CancelAppointment cancelAppointment;

	@BeforeEach
	void setUp() {
		cancelAppointment = new CancelAppointment(appointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	public void testRunSuccess(){
		when(appointmentPublicApiPermissions.canCancelAppointment(institutionId)).thenReturn(true);

		cancelAppointment.run(institutionId, appointmentId, reason);

		verify(appointmentPort, times(1)).cancelAppointment(institutionId, appointmentId, reason);
	}

	@Test
	public void cancelAppointmentsAccessDenied(){
		when(appointmentPublicApiPermissions.canCancelAppointment(institutionId)).thenReturn(false);
		assertThrows(CancelAppointmentAccessDeniedException.class , () ->
				cancelAppointment.run(institutionId, appointmentId, reason));
	}
}
