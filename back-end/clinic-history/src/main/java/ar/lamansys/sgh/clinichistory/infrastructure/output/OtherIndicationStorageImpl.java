package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OtherIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherIndication;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtherIndicationStorageImpl implements OtherIndicationStorage {

	private final OtherIndicationRepository otherIndicationRepository;

	private final SharedHospitalUserPort sharedHospitalUserPort;

	private final DosageRepository dosageRepository;


	@Override
	public Integer createOtherIndication(OtherIndicationBo otherIndicationBo) {
		log.debug("Input parameter -> otherIndicationBo {}", otherIndicationBo);
		Integer dosageId = dosageRepository.save(mapToDosage(otherIndicationBo.getDosage())).getId();
		otherIndicationBo.getDosage().setId(dosageId);
		Integer result = otherIndicationRepository.save(mapToEntity(otherIndicationBo)).getId();
		log.debug("Output -> {}", result);
		return result;
	}

	private Dosage mapToDosage(DosageBo bo){
		Dosage result = new Dosage();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setStartDate(bo.getStartDate());
		result.setEndDate(bo.getEndDate());
		return result;
	}

	private OtherIndication mapToEntity(OtherIndicationBo bo) {
		OtherIndication result = new OtherIndication();
		result.setId(bo.getId());
		result.setPatientId(bo.getPatientId());
		result.setTypeId(bo.getTypeId());
		result.setStatusId(bo.getStatusId());
		result.setIndicationDate(bo.getIndicationDate());
		result.setProfessionalId(bo.getProfessionalId());
		result.setOtherIndicationType(bo.getOtherIndicationTypeId());
		result.setDosageId(bo.getDosage().getId());
		result.setDescription(bo.getDescription());
		result.setOtherType(bo.getOtherType());
		return result;
	}

}