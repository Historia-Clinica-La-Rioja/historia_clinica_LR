package net.pladema.patient.mergeclinichistory;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.SharedNursingConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.patient.application.mergepatient.MigrateAppointment;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import net.pladema.patient.application.port.MigratePatientStorage;
import net.pladema.patient.infrastructure.output.repository.MigratableRepositoryMap;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReportRepository;

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
