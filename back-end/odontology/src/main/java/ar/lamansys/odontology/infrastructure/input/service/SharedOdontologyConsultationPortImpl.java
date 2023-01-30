package ar.lamansys.odontology.infrastructure.input.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyConsultationInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SharedOdontologyConsultationPortImpl implements SharedOdontologyConsultationPort {

	private final OdontologyConsultationStorage odontologyConsultationStorage;

	@Override
	public List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients) {
		return odontologyConsultationStorage.getOdontologyConsultationIdsFromPatients(patients);
	}

	@Override
	public List<OdontologyConsultationInfoDto> findAllById(List<Integer> ids) {
		return odontologyConsultationStorage.findAllById(ids).stream().map(oc -> new OdontologyConsultationInfoDto(
				oc.getId(),
				oc.getPatientId(),
				oc.getClinicalSpecialtyId(),
				oc.getInstitutionId(),
				oc.getPatientMedicalCoverageId(),
				oc.getDoctorId(),
				oc.getPerformedDate(),
				oc.getBillable())).collect(Collectors.toList());
	}
}
