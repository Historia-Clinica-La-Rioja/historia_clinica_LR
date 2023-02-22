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
import net.pladema.patient.application.mergepatient.MigrateInternmentEpisode;

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
public class MigrateInternmentEpisodeTest {

	private MigrateInternmentEpisode migrateInternmentEpisode;

	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;


	@BeforeEach
	void setUp() {
		migrateInternmentEpisode = new MigrateInternmentEpisode(mergeClinicHistoryStorage);
	}

	@Test
	void migrateInternmentEpisode_run_completed() {
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		Integer newPatientId = 3;

		List<Integer> ieIds = Arrays.asList(10,15);
		List<Integer> srIds = Arrays.asList(20,25);

		when(mergeClinicHistoryStorage.getInternmentEpisodesIds(oldPatientsIds))
				.thenReturn(ieIds);
		when(mergeClinicHistoryStorage.getServiceRequestIdsFromIdSourceType(ieIds, ESourceType.HOSPITALIZATION.getId()))
				.thenReturn(srIds);

		List<Integer> consultationIds = new ArrayList<>() {{
			addAll(ieIds);
			addAll(srIds);
		}};

		List<Long> documentIds = Arrays.asList(30L,31L,32L,33L);

		when(mergeClinicHistoryStorage.getDocumentsIds(consultationIds, Arrays.asList(
				ESourceType.HOSPITALIZATION,
				ESourceType.ORDER)))
				.thenReturn(documentIds);

		migrateInternmentEpisode.execute(oldPatientsIds,newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).getInternmentEpisodesIds(oldPatientsIds);
		verify(mergeClinicHistoryStorage, times(1)).getServiceRequestIdsFromIdSourceType(ieIds,ESourceType.HOSPITALIZATION.getId());

		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(HealthConditionRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(AllergyIntoleranceRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ImmunizationRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(MedicationStatementRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ProceduresRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationRiskFactorRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(ObservationLabRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(DiagnosticReportRepository.class, documentIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(IndicationRepository.class, documentIds, newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).modifyInternmentEpisode(ieIds, newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyServiceRequest(srIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyDocument(documentIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).rebuildDocumentsFiles(documentIds);

	}
}
