package net.pladema.medicine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineFinancingStatusRepository;

import net.pladema.medicine.infrastructure.output.repository.MedicineFinancingStatusRepository;

import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineFinancingStatus;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineFinancingStatus;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AssociateAllMedicinesToInstitution {

	private final InstitutionMedicineFinancingStatusRepository institutionMedicineFinancingStatusRepository;
	private final MedicineFinancingStatusRepository medicineFinancingStatusRepository;

	public void run(Integer institutionId){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<Integer> associatedMedicines = institutionMedicineFinancingStatusRepository.getByInstitutionId(institutionId).stream().map(InstitutionMedicineFinancingStatusBo::getId).collect(Collectors.toList());

		List<Integer> medicineIds = medicineFinancingStatusRepository.findAll()
				.stream()
				.map(MedicineFinancingStatus::getId)
				.filter(id -> !associatedMedicines.contains(id))
				.collect(Collectors.toList());

		medicineIds.forEach(id -> institutionMedicineFinancingStatusRepository.save(new InstitutionMedicineFinancingStatus(institutionId, id)));
	}

}
