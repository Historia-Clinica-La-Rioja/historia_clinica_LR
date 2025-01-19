package net.pladema.snowstorm.application.fetchmedicineconceptswithfundingdata;

import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditFinancedBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.application.fetchconcepts.FetchConcepts;
import net.pladema.snowstorm.domain.SnomedFinancedMedicineBo;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.application.port.output.ValidateFinancingMedicationPort;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class FetchMedicineConceptsWithFinancingData {

	private final FetchConcepts fetchConcepts;

	private final ValidateFinancingMedicationPort validateFinancingMedicationPort;

	public List<SnomedFinancedMedicineBo> run(Integer institutionId, String term, String problemSctid) throws SnowstormApiException {
		log.debug("Input -> institutionId: {},  term: {}, problemSctid: {} ", institutionId, term, problemSctid);
		var concepts = fetchConcepts.run(term, SnomedECL.MEDICINE_WITH_UNIT_OF_PRESENTATION.name());
		List<String> medicineSctids = concepts.stream().map(SnomedSearchItemBo::getConceptId).collect(Collectors.toList());
		var medicinesWithFundingInfo = validateFinancingMedicationPort.validateFinancingByInstitutionAndProblem(institutionId, medicineSctids, problemSctid);
		var result = processData(concepts, medicinesWithFundingInfo);
		log.debug("Output -> {}", result);
		return result;
	}

	private List<SnomedFinancedMedicineBo> processData(List<SnomedSearchItemBo> medicines, Map<String, MedicineGroupAuditFinancedBo> medicinesWithCoveredInfo) {
		return medicines.stream()
				.map(m -> new SnomedFinancedMedicineBo(m.getConceptId(), m.getId(), m.getFsn(), m.getPt(), medicinesWithCoveredInfo.get(m.getConceptId()).getFinanced(), medicinesWithCoveredInfo.get(m.getConceptId()).getAuditRequiredDescription()))
				.sorted(Comparator.comparing(SnomedFinancedMedicineBo::isFinanced)
						.reversed()
						.thenComparing(medicine -> medicine.getPt().getTerm()))
				.collect(Collectors.toList());
	}
}
