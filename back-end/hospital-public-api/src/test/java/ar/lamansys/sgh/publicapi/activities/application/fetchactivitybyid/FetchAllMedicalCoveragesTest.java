package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid;

import static org.mockito.Mockito.when;

import java.util.List;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.FetchAllMedicalCoverages;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.exception.FetchAllMedicalCoveragesDeniedAccessException;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.port.out.FetchAllMedicalCoveragesStorage;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.infrastructure.input.rest.FetchAllMedicalCoveragesController;

@ExtendWith(MockitoExtension.class)
public class FetchAllMedicalCoveragesTest {


	private FetchAllMedicalCoveragesController fetchAllMedicalCoveragesController;

	@Mock
	private FetchAllMedicalCoveragesStorage fetchAllMedicalCoveragesStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	public void setup() {
		FetchAllMedicalCoverages fetchAllMedicalCoverages =
				new FetchAllMedicalCoverages(fetchAllMedicalCoveragesStorage, activitiesPublicApiPermissions);
		this.fetchAllMedicalCoveragesController =
				new FetchAllMedicalCoveragesController(fetchAllMedicalCoverages);
	}

	@Test
	void FetchAllMedicalCoveragesDeniedAccessException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(FetchAllMedicalCoveragesDeniedAccessException.class,
				() -> fetchAllMedicalCoveragesController.getAllMedicalCoverages());
	}

	@Test
	void successFetchAllMedicalCoverages(){
		allowAccessPermission(true);
		when(fetchAllMedicalCoveragesStorage.fetchAll()).thenReturn(List.of(mockResult()));
		var result = fetchAllMedicalCoveragesController.getAllMedicalCoverages();
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.size(), 1);
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canFetchAllMedicalCoverages())
				.thenReturn(canAccess);
	}

	private MedicalCoverageDataDto mockResult(){
		return MedicalCoverageDataDto.builder()
				.name("mockName")
				.cuit("mockCuit")
				.type("OBRA SOCIAL")
				.rnos("mockRnos")
				.build();
	}
}
