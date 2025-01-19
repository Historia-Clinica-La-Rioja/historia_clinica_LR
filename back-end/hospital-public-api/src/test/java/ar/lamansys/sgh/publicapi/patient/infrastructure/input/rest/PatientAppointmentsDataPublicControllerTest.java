package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.application.fetchappointmentsdatabydni.FetchAppointmentsDataByDni;
import ar.lamansys.sgh.publicapi.patient.domain.AppointmentsByUserBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PatientAppointmentsDataPublicControllerTest {

	@Mock
	private FetchAppointmentsDataByDni fetchAppointmentsDataByDni;

	private PatientAppointmentsDataPublicController patientAppointmentsDataPublicController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		patientAppointmentsDataPublicController = new PatientAppointmentsDataPublicController(fetchAppointmentsDataByDni);
	}

	@Test
	public void testGetAppointments() {
		String identificationNumber = "12345678";
		Short identificationTypeId = 1;
		Short genderId = 1;
		String birthdate = "2000-01-01";

		AppointmentsByUserBo appointmentsByUserBo = new AppointmentsByUserBo();
		ResponseEntity responseEntity = patientAppointmentsDataPublicController.getAppointments(identificationNumber, identificationTypeId, genderId, birthdate);
		when(fetchAppointmentsDataByDni.run(identificationNumber,identificationTypeId,genderId,birthdate)).thenReturn(appointmentsByUserBo);
		assertEquals(200, responseEntity.getStatusCodeValue());
	}
}