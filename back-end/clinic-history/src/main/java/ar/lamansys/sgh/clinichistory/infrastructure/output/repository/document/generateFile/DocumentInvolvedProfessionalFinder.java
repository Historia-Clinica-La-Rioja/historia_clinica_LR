package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentInvolvedProfessionalFinder {

	private final SharedStaffPort sharedStaffPort;

	public List<ProfessionalCompleteDto> find(List<Integer> healthcareProfessionalIds) {
		return sharedStaffPort.getProfessionalsCompleteByIds(healthcareProfessionalIds);
	}

}
