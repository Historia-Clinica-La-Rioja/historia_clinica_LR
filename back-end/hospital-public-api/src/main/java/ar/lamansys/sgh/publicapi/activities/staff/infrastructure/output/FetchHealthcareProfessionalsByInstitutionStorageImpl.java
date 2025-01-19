package ar.lamansys.sgh.publicapi.activities.staff.infrastructure.output;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.staff.application.port.out.FetchHealthcareProfessionalsByInstitutionStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchHealthcareProfessionalsByInstitutionStorageImpl implements FetchHealthcareProfessionalsByInstitutionStorage {

	private SharedStaffPort sharedStaffPort;

	@Override
	public List<MedicineDoctorCompleteDto> fetch(Integer institutionId) {
		return sharedStaffPort.getProfessionalsCompleteByInstitutionId(institutionId);
	}
}
