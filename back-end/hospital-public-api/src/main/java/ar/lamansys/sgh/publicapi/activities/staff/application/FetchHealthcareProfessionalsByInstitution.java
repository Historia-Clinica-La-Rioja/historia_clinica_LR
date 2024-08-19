package ar.lamansys.sgh.publicapi.activities.staff.application;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.staff.application.exception.FetchHealthcareProfessionalsDeniedException;
import ar.lamansys.sgh.publicapi.activities.staff.application.exception.InstitutionNotExistsException;
import ar.lamansys.sgh.publicapi.activities.staff.application.port.out.FetchHealthcareProfessionalsByInstitutionStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchHealthcareProfessionalsByInstitution {

	private final FetchHealthcareProfessionalsByInstitutionStorage fetchHealthcareProfessionalsByInstitutionStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<MedicineDoctorCompleteDto> run(String refsetCode) {

		var institutionId = activitiesPublicApiPermissions.findInstitutionId(refsetCode);

		if(institutionId.isEmpty()){
			throw new InstitutionNotExistsException();
		}

		if(activitiesPublicApiPermissions.canFetchHealthcareProfessionals(institutionId.get())) {
			return fetchHealthcareProfessionalsByInstitutionStorage.fetch(institutionId.get());
		}

		throw new FetchHealthcareProfessionalsDeniedException();

	}
}
