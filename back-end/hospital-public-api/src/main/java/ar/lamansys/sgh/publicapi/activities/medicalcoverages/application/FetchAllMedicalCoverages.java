package ar.lamansys.sgh.publicapi.activities.medicalcoverages.application;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.exception.FetchAllMedicalCoveragesDeniedAccessException;
import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.port.out.FetchAllMedicalCoveragesStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchAllMedicalCoverages {
	private final FetchAllMedicalCoveragesStorage fetchAllMedicalCoveragesStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<MedicalCoverageDataDto> run() {
		if(activitiesPublicApiPermissions.canFetchAllMedicalCoverages()){
			return fetchAllMedicalCoveragesStorage.fetchAll();
		}
		throw new FetchAllMedicalCoveragesDeniedAccessException();
	}
}
