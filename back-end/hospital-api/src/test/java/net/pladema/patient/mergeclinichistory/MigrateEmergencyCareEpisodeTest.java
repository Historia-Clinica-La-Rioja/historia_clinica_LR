package net.pladema.patient.mergeclinichistory;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import net.pladema.patient.application.mergepatient.MigrateEmergencyCareEpisode;

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
public class MigrateEmergencyCareEpisodeTest {

	private MigrateEmergencyCareEpisode migrateEmergencyCareEpisode;

	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@BeforeEach
	void setUp() {
		migrateEmergencyCareEpisode = new MigrateEmergencyCareEpisode(mergeClinicHistoryStorage);
	}

	@Test
	void migrateEmergencyCareEpisode_run_completed() {
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		Integer newPatientId = 3;

		List<Integer> eceIds = Arrays.asList(10,15);

		when(mergeClinicHistoryStorage.getEmergencyCareEpisodeIds(oldPatientsIds))
				.thenReturn(eceIds);

		List<Long> documentIds = Arrays.asList(30L,35L);

		when(mergeClinicHistoryStorage.getDocumentsIds(eceIds, ESourceType.EMERGENCY_CARE))
				.thenReturn(documentIds);

		migrateEmergencyCareEpisode.execute(oldPatientsIds,newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).getEmergencyCareEpisodeIds(oldPatientsIds);

		verify(mergeClinicHistoryStorage, times(1)).migratePatientIdFromItem(HealthConditionRepository.class, documentIds, newPatientId);

		verify(mergeClinicHistoryStorage, times(1)).modifyDocument(documentIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).rebuildDocumentsFiles(documentIds);
		verify(mergeClinicHistoryStorage, times(1)).modifyTriageRiskFactor(eceIds,newPatientId);
		verify(mergeClinicHistoryStorage, times(1)).modifyEmergencyCareEpisode(eceIds,newPatientId);
	}
}
