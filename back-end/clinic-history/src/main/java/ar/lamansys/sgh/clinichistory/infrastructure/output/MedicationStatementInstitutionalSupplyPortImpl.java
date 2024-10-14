package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.output.MedicationStatementInstitutionalSupplyPort;

import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyMedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementInstitutionalSupplyRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatementInstitutionalSupply;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MedicationStatementInstitutionalSupplyPortImpl implements MedicationStatementInstitutionalSupplyPort {

	private final MedicationStatementInstitutionalSupplyRepository medicationStatementInstitutionalSupplyRepository;

	private final MedicationStatementRepository medicationStatementRepository;

	@Override
	public Integer save(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupplyBo) {
		short DISPENSED_STATE_ID = 2;
		List<MedicationStatementInstitutionalSupply> entities = parseToMedicationStatementInstitutionalSupplyEntities(saveMedicationStatementInstitutionalSupplyBo);
		medicationStatementInstitutionalSupplyRepository.saveAll(entities);
		medicationStatementRepository.updateMedicationStatementLineStateById(saveMedicationStatementInstitutionalSupplyBo.getMedicationStatementId(), DISPENSED_STATE_ID);
		return 1;
	}

	private List<MedicationStatementInstitutionalSupply> parseToMedicationStatementInstitutionalSupplyEntities(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupplyBo) {
		return saveMedicationStatementInstitutionalSupplyBo.getMedications().stream()
				.map(medication -> parseToMedicationStatementInstitutionalSupplyEntity(saveMedicationStatementInstitutionalSupplyBo, medication))
				.collect(Collectors.toList());
	}

	private MedicationStatementInstitutionalSupply parseToMedicationStatementInstitutionalSupplyEntity(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupplyBo, SaveMedicationStatementInstitutionalSupplyMedicationBo medication) {
		MedicationStatementInstitutionalSupply result = new MedicationStatementInstitutionalSupply();
		result.setInstitutionId(saveMedicationStatementInstitutionalSupplyBo.getInstitutionId());
		result.setMedicationStatementId(saveMedicationStatementInstitutionalSupplyBo.getMedicationStatementId());
		result.setSnomedId(medication.getSnomedId());
		result.setQuantity(medication.getQuantity());
		return result;
	}

}
