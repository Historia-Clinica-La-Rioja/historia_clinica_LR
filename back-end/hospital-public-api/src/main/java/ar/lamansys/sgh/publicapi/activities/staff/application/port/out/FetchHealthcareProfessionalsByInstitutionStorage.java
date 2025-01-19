package ar.lamansys.sgh.publicapi.activities.staff.application.port.out;


import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;

public interface FetchHealthcareProfessionalsByInstitutionStorage {

	List<MedicineDoctorCompleteDto> fetch(Integer institutionId);
}
