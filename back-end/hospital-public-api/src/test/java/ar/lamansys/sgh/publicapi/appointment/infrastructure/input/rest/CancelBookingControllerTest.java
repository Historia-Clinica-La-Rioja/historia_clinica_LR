package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.CancelBookingByInstitution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CancelBookingControllerTest {

	@Mock
	private CancelBookingByInstitution cancelBookingByInstitution;

	private CancelBookingController cancelBookingController;

	@BeforeEach
	public void setUp() {
		cancelBookingController = new CancelBookingController(cancelBookingByInstitution);
	}

	@Test
	void testCancelBooking() {
		Integer institutionId = 1;
		String uuid = "uuid";

		cancelBookingController.cancelBooking(institutionId, uuid);

		verify(cancelBookingByInstitution, times(1)).run(institutionId, uuid);
	}
}
