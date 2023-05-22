package net.pladema.clinichistory.outpatient.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.port.OutpatientConsultationStorage;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryStorage;

import org.springframework.stereotype.Service;

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
	public Map<Integer, List<OutpatientBasicDataBo>> getOutpatientConsultationsToCipres() {
		List<OutpatientBasicDataBo> outpatientConsultations = outpatientConsultationSummaryStorage.getOutpatientConsultationsToCipres(LIMIT);

		List<Integer> patientIds = outpatientConsultations.stream().map(oc -> oc.getPatient().getId()).distinct().collect(Collectors.toList());

		Map<Integer, List<OutpatientBasicDataBo>> result = new HashMap<>();

		patientIds.forEach(patientId -> {
			List<OutpatientBasicDataBo> outpatientConsultationsFromPatient = outpatientConsultations.stream().filter(oc -> oc.getPatient().getId().equals(patientId)).collect(Collectors.toList());
			List<Integer> outpatientConsultationIds = outpatientConsultationsFromPatient.stream().map(OutpatientBasicDataBo::getId).collect(Collectors.toList());
			List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryStorage.getHealthConditionsByPatient(patientId, outpatientConsultationIds);
			List<ProcedureSummaryBo> procedures = outpatientConsultationSummaryStorage.getProceduresByPatient(patientId, outpatientConsultationIds);

			outpatientConsultationsFromPatient.stream().forEach(oc -> {
					oc.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oc.getId())).map(p -> new SnomedBo(p.getSnomedSctid(), p.getSnomedPt())).collect(Collectors.toList()));
					oc.setProblems(healthConditions.stream().filter(h -> h.getConsultationID().equals(oc.getId())).map(p -> new SnomedBo(p.getSnomedSctid(), p.getSnomedPt())).collect(Collectors.toList()));
					oc.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(oc.getDocumentId()));
					oc.setRiskFactorData(documentService.getRiskFactorStateFromDocument(oc.getDocumentId()));
			});
			result.put(patientId, outpatientConsultationsFromPatient);
		});
		log.debug("Output size -> {} ", result.size());
		return result;
	}

}
