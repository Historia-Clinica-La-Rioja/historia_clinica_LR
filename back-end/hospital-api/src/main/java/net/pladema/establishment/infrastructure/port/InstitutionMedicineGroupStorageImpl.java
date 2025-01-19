package net.pladema.establishment.infrastructure.port;


import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditBo;
import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditFinancedBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.InstitutionMedicineGroupStorage;

import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
@Service
public class InstitutionMedicineGroupStorageImpl implements InstitutionMedicineGroupStorage {

	private final InstitutionMedicineGroupRepository institutionMedicineGroupRepository;

	@Override
	public Map<String, MedicineGroupAuditFinancedBo> validateFinancingMedicinesByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids,
																				String problemSctid) {
		log.debug("Input parameters -> institutionId: {}, medicineSctids: {}, problemSctid: {}", institutionId, medicineSctids, problemSctid);
		List<MedicineGroupAuditBo> coveredMedicines = institutionMedicineGroupRepository.validateFinancingByInstitutionAndProblem(institutionId, medicineSctids, problemSctid);
		Map<String, MedicineGroupAuditFinancedBo> result = new HashMap<>();
		medicineSctids.forEach(m -> result.put(m, this.findMedicineCovered(coveredMedicines, m)));
		log.debug("Output result -> {}", result);
		return result;
	}



	private MedicineGroupAuditFinancedBo findMedicineCovered(List<MedicineGroupAuditBo> coveredMedicines, String medicine ) {
		Stream<MedicineGroupAuditBo> result = coveredMedicines.stream().filter(covered -> medicine.equals(covered.getSctidMedicine()));
		List<String> auditRequiredTexts = result.map(MedicineGroupAuditBo::getAuditRequiredDescription).collect(Collectors.toList());
		boolean hasElements = !auditRequiredTexts.isEmpty();
		return hasElements ? new MedicineGroupAuditFinancedBo(true, auditRequiredTexts) : new MedicineGroupAuditFinancedBo(false, null);
	}

}


