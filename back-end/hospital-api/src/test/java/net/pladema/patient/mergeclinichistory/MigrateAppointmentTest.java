package net.pladema.patient.mergeclinichistory;

import net.pladema.patient.application.mergepatient.MigrateAppointment;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MigrateAppointmentTest {

	private MigrateAppointment migrateAppointment;
	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@BeforeEach
	void setUp() {
		migrateAppointment = new MigrateAppointment(mergeClinicHistoryStorage);
	}

	@Test
	void migrateAppointment_run_completed() {
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		Integer newPatientId = 3;

		migrateAppointment.execute(oldPatientsIds,newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).modifyAppointment(oldPatientsIds,newPatientId);
	}
}
