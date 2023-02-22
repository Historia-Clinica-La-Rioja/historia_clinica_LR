package net.pladema.patient.mergeclinichistory;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import net.pladema.patient.application.mergepatient.MigrateOdontologyConsultation;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MigrateOdontologyConsultationTest {

	private MigrateOdontologyConsultation migrateOdontologyConsultation;

	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@BeforeEach
	void setUp() {
		migrateOdontologyConsultation = new MigrateOdontologyConsultation(mergeClinicHistoryStorage);
	}

	@Test
	void migrateOdontologyConsultation_run_completed(){
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		Integer newPatientId = 3;

		List<Integer> ocIds = Arrays.asList(10,15);

		when(mergeClinicHistoryStorage.getOdontologyConsultationIds(oldPatientsIds))
				.thenReturn(ocIds);

		List<Long> documentIds = Arrays.asList(30L,35L);

		when(mergeClinicHistoryStorage.getDocumentsIds(ocIds, Arrays.asList(
				ESourceType.ODONTOLOGY)))
				.thenReturn(documentIds);

		migrateOdontologyConsultation.execute(oldPatientsIds,newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).getOdontologyConsultationIds(oldPatientsIds);

		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(HealthConditionRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(AllergyIntoleranceRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ImmunizationRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(MedicationStatementRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ProceduresRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(OdontologyDiagnosticRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(OdontologyProcedureRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationRiskFactorRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationLabRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(DiagnosticReportRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(IndicationRepository.class, documentIds, newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).modifyDocument(documentIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).rebuildDocumentsFiles(documentIds);
		verify(mergeClinicHistoryStorage, times(1)).modifyOdontogram(newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyOdontologyConsultation(ocIds,newPatientId);
	}
}
