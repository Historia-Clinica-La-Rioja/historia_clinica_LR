package net.pladema.clinichistory.outpatient.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.MedicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.port.OutpatientConsultationStorage;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryStorage;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutpatientConsultationStorageImpl implements OutpatientConsultationStorage {

	private static final Integer LIMIT = 500;
	
	private final OutpatientConsultationSummaryStorage outpatientConsultationSummaryStorage;

	private final DocumentService documentService;

	@Override
	public List<OutpatientBasicDataBo> getOutpatientConsultationsToCipres() {
		List<OutpatientBasicDataBo> result = outpatientConsultationSummaryStorage.getOutpatientConsultationsToCipres(LIMIT);
		List<Integer> outpatientConsultationIds = result.stream().map(OutpatientBasicDataBo::getId).collect(Collectors.toList());

		List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryStorage.getHealthConditionsByOutpatientIds(outpatientConsultationIds);
		List<ProcedureSummaryBo> procedures = outpatientConsultationSummaryStorage.getProceduresByOutpatientIds(outpatientConsultationIds);
		List<MedicationSummaryBo> medications = outpatientConsultationSummaryStorage.getMedicationsByOutpatientIds(outpatientConsultationIds);

		Map<Integer, List<SnomedBo>> procedureMap = procedures.stream()
				.collect(Collectors.groupingBy(ProcedureSummaryBo::getConsultationId,
						Collectors.mapping(p -> new SnomedBo(p.getSnomedSctid(), p.getSnomedPt()), Collectors.toList())));

		Map<Integer, List<SnomedBo>> healthConditionMap = healthConditions.stream()
				.collect(Collectors.groupingBy(HealthConditionSummaryVo::getConsultationID,
						Collectors.mapping(hc -> new SnomedBo(hc.getSnomedSctid(), hc.getSnomedPt()), Collectors.toList())));

		Map<Integer, List<SnomedBo>> medicationMap = medications.stream()
				.collect(Collectors.groupingBy(MedicationSummaryBo::getEncounterId,
						Collectors.mapping(m -> new SnomedBo(m.getSnomedSctid(), m.getSnomedPt()), Collectors.toList())));

		result.forEach(oc -> {
			oc.setProcedures(procedureMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setProblems(healthConditionMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setMedications(medicationMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(oc.getDocumentId()));
			oc.setRiskFactorData(documentService.getRiskFactorStateFromDocument(oc.getDocumentId()));
		});

		log.debug("Output size -> {} ", result.size());
		return result;
	}

}
