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
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import net.pladema.patient.application.mergepatient.MigrateOutpatientConsultation;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MigrateOutpatientConsultationTest {

	private MigrateOutpatientConsultation migrateOutpatientConsultation;

	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@BeforeEach
	void setUp() {
		migrateOutpatientConsultation = new MigrateOutpatientConsultation(mergeClinicHistoryStorage);
	}

	@Test
	void migrateOutpatientConsultation_run_completed() {
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		Integer newPatientId = 3;

		List<Integer> ocIds = Arrays.asList(10);
		List<Integer> ncIds = Arrays.asList(11);
		List<Integer> mrIds = Arrays.asList(12,13);
		List<Integer> srIds = Arrays.asList(14);
		List<Integer> vcIds = Arrays.asList(15);
		List<Integer> crIds = Arrays.asList(16);

		when(mergeClinicHistoryStorage.getOutpatientConsultationIds(oldPatientsIds))
				.thenReturn(ocIds);
		when(mergeClinicHistoryStorage.getNursingConsultationIds(oldPatientsIds))
				.thenReturn(ncIds);
		when(mergeClinicHistoryStorage.getMedicationRequestIds(oldPatientsIds))
				.thenReturn(mrIds);
		when(mergeClinicHistoryStorage.getServiceRequestIds(oldPatientsIds))
				.thenReturn(srIds);
		when(mergeClinicHistoryStorage.getVaccineConsultationIds(oldPatientsIds))
				.thenReturn(vcIds);
		when(mergeClinicHistoryStorage.getCounterReferenceIds(oldPatientsIds))
				.thenReturn(crIds);

		List<Integer> consultationIds = new ArrayList<>() {{
			addAll(ocIds);
			addAll(ncIds);
			addAll(mrIds);
			addAll(srIds);
			addAll(vcIds);
			addAll(crIds);
		}};

		List<Long> documentOutpatientIds = Arrays.asList(30L);
		List<Long> documentNursingIds = Arrays.asList(31L);
		List<Long> documentMedicationIds = Arrays.asList(32L,33L);
		List<Long> documentOrderIds = Arrays.asList(34L);
		List<Long> documentVaccineIds = Arrays.asList(35L);
		List<Long> documentReferenceIds = Arrays.asList(36L,37L,38L,39L);

		List<Long> documentIds = Arrays.asList(30L,31L,32L,33L,34L,35L,36L,37L,38L,39L);

		when(mergeClinicHistoryStorage.getDocumentsIds(ocIds, ESourceType.OUTPATIENT))
				.thenReturn(documentOutpatientIds);

		when(mergeClinicHistoryStorage.getDocumentsIds(ncIds, ESourceType.NURSING))
				.thenReturn(documentNursingIds);

		when(mergeClinicHistoryStorage.getDocumentsIds(mrIds, ESourceType.RECIPE))
				.thenReturn(documentMedicationIds);

		when(mergeClinicHistoryStorage.getDocumentsIds(srIds, ESourceType.ORDER))
				.thenReturn(documentOrderIds);

		when(mergeClinicHistoryStorage.getDocumentsIds(vcIds, ESourceType.IMMUNIZATION))
				.thenReturn(documentVaccineIds);

		when(mergeClinicHistoryStorage.getDocumentsIds(crIds, ESourceType.COUNTER_REFERENCE))
				.thenReturn(documentReferenceIds);

		migrateOutpatientConsultation.execute(oldPatientsIds,newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).getOutpatientConsultationIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getNursingConsultationIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getMedicationRequestIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getServiceRequestIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getVaccineConsultationIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getCounterReferenceIds(oldPatientsIds);

		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(HealthConditionRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(AllergyIntoleranceRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ImmunizationRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(MedicationStatementRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ProceduresRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationRiskFactorRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationLabRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(DiagnosticReportRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(IndicationRepository.class, documentIds, newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).modifyOutpatientConsultation(ocIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyNursingConsultation(ncIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyMedicationRequest(mrIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyServiceRequest(srIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyVaccineConsultation(vcIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyCounterReference(crIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyDocument(documentIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).rebuildDocumentsFiles(documentIds);
		verify(mergeClinicHistoryStorage, times(1)).modifySnvsReport(oldPatientsIds, newPatientId);

	}
}
