package net.pladema.reports.application.encountertoappointment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.reports.application.ports.EncounterToAppointmentPort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EncounterToAppointmentServiceTest {

	EncounterToAppointmentService service;

	@Mock
	EncounterToAppointmentPort encounterToAppointmentPort;

	@Mock
	DocumentAppointmentService documentAppointmentService;

	@BeforeEach
	void setup() {
		service = new EncounterToAppointmentService(encounterToAppointmentPort, documentAppointmentService);
	}

	@Test
	public void returns_empty_when_source_not_supported() {
		when(encounterToAppointmentPort.supportsSourceType(any())).thenReturn(false);
		assertEquals(service.run(1,(short)1), Optional.empty());
	}

}
