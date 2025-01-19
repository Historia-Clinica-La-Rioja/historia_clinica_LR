package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.CancelAppointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CancelAppointmentControllerTest {

	@Mock
	private CancelAppointment cancelAppointment;

	private CancelAppointmentController cancelAppointmentController;

	@BeforeEach
	void setUp() {
		cancelAppointmentController = new CancelAppointmentController(cancelAppointment);
	}

	@Test
	void testCancelAppointment() {
		ResponseEntity<String> response = cancelAppointmentController.cancelAppointment(1, 1, "No longer needed");
		assertEquals(200, response.getStatusCodeValue());
	}

}
