package net.pladema.medicine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAuditRequiredMedicineGroups {

	private final MedicineGroupRepository medicineGroupRepository;

	public List<InstitutionMedicineGroupBo> run(
			String medicineId,
			String problemId
	) {
		log.debug("Input parameters -> medicineId {}, problemId {}", medicineId, problemId);

		List<InstitutionMedicineGroupBo> finalList = medicineGroupRepository.getAllMedicineGroupByProblemAndMedicine(problemId, medicineId);

		log.debug("Output -> finalFilteredList {}",finalList);
		return finalList;
	}

}
