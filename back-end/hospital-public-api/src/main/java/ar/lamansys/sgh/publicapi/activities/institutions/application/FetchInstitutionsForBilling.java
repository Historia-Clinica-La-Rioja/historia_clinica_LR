package ar.lamansys.sgh.publicapi.activities.institutions.application;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.institutions.application.exception.FetchInstitutionsForBillingAccessException;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchInstitutionsForBilling {
	private final SharedInstitutionPort sharedInstitutionPort;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;


	public List<InstitutionInfoDto> fetch(){
		assertUserCanAccess();
		return sharedInstitutionPort.fetchInstitutions();
	}

	private void assertUserCanAccess() {
		if (!activitiesPublicApiPermissions.canFetchAllInstitutions())
			throw new FetchInstitutionsForBillingAccessException();
	}


}
