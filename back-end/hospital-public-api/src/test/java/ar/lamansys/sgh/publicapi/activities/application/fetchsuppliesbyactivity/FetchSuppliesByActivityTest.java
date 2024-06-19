package ar.lamansys.sgh.publicapi.activities.application.fetchsuppliesbyactivity;
import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FetchSuppliesByActivityTest {

	private FetchSuppliesByActivity fetchSuppliesByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setup() {
		fetchSuppliesByActivity = new FetchSuppliesByActivity(activityInfoStorage, activitiesPublicApiPermissions);
	}

	@Test
	void procedureSuccess() {
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;

		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		when(activityInfoStorage.getSuppliesByActivity(refsetCode, activityId)).thenReturn(
				Collections.singletonList(SupplyInformationBo.builder()
						.supplyType("Vacuna")
						.status("Completo")
						.snomedBo(new SnomedBo("1", "1"))
						.build())
		);

		List<SupplyInformationBo> result = fetchSuppliesByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 1);
	}

	@Test
	void failSuppliesByActivityAccessDeniedException(){
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivitiesAccessDeniedException.class,
				() -> fetchSuppliesByActivity.run("",10L));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessActivityInfo(1)).thenReturn(canAccess);
	}

}