package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.institutions.application.FetchInstitutionsForBilling;
import ar.lamansys.sgh.publicapi.activities.institutions.application.exception.FetchInstitutionsForBillingAccessException;
import ar.lamansys.sgh.publicapi.activities.institutions.infrastructure.input.rest.InstitutionsForBillingController;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;

@ExtendWith(MockitoExtension.class)
public class FetchAllInstitutionsControllerTest {

	@Mock
	SharedInstitutionPort sharedInstitutionPort;

	@Mock
	ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	InstitutionsForBillingController institutionsForBillingController;

	FetchInstitutionsForBilling fetchInstitutionsForBilling;

	@BeforeEach
	public void setUp() {
		fetchInstitutionsForBilling = new FetchInstitutionsForBilling(sharedInstitutionPort, activitiesPublicApiPermissions);
		institutionsForBillingController = new InstitutionsForBillingController(fetchInstitutionsForBilling);
	}

	@Test
	void success(){
		allowAccessPermission(true);
		when(sharedInstitutionPort.fetchInstitutions()).thenReturn(mockResponse());
		var result = institutionsForBillingController.getInstitutionsForBilling();
		assertEquals(2, result.size());
		assertEquals(1, (int) result.stream().filter(inst -> inst.getId().equals(1)).count());
		assertEquals(1, (int) result.stream().filter(inst -> inst.getName().equals("Inst1")).count());
		assertEquals(1, (int) result.stream().filter(inst -> inst.getSisaCode().equals("Sisa2")).count());
	}

	@Test
	void failAccess(){
		allowAccessPermission(false);
		TestUtils.shouldThrow(FetchInstitutionsForBillingAccessException.class,
				() -> institutionsForBillingController.getInstitutionsForBilling());
	}

	private List<InstitutionInfoDto> mockResponse() {
		InstitutionInfoDto inst1 = new InstitutionInfoDto(1, "Inst1", "Sisa1");
		InstitutionInfoDto inst2 = new InstitutionInfoDto(2, "Inst2", "Sisa2");
		return List.of(inst1, inst2);
	}


	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canFetchAllInstitutions())
				.thenReturn(canAccess);
	}
}
