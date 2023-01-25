package ar.lamansys.nursing.infrastructure.input.service;

import ar.lamansys.nursing.application.port.NursingConsultationStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.NursingConsultationInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.SharedNursingConsultationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedNursingConsultationPortImpl implements SharedNursingConsultationPort {

	private final NursingConsultationStorage nursingConsultationStorage;

	@Override
	public List<Integer> getNursingConsultationIdsFromPatients(List<Integer> patients) {
		return nursingConsultationStorage.getNursingConsultationIdsFromPatients(patients);
	}

	@Override
	public List<NursingConsultationInfoDto> findAllById(List<Integer> ids) {
		return nursingConsultationStorage.findAllByIds(ids).stream()
				.map(nc -> new NursingConsultationInfoDto(nc.getId(),
						nc.getPatientId(),
						nc.getPatientMedicalCoverageId(),
						nc.getClinicalSpecialtyId(),
						nc.getInstitutionId(),
						nc.getDoctorId(),nc.getBillable(),
						nc.getPerformedDate())).collect(Collectors.toList());
	}
}
