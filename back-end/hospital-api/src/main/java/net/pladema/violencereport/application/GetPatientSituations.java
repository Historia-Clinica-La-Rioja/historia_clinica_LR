package net.pladema.violencereport.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.domain.ViolenceReportSituationBo;

import net.pladema.violencereport.infrastructure.output.repository.GetPatientSituationListRepository;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceModalityRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceTypeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GetPatientSituations {

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceTypeRepository violenceTypeRepository;

	private ViolenceModalityRepository violenceModalityRepository;

	private GetPatientSituationListRepository getPatientSituationListRepository;

	public Page<ViolenceReportSituationBo> run(Integer patientId, Boolean mustBeLimited, Pageable pageable) {
		log.debug("Input parameters -> patientId {}, mustBeLimited {}, pageable {} ", patientId, mustBeLimited, pageable);
		Page<ViolenceReportSituationBo> result = getPatientSituationListRepository.getPatientSituations(patientId, mustBeLimited, pageable);
		result.forEach(situation -> getSituationViolenceTypesAndModalities(patientId, situation));
		log.debug("Output -> {}", result);
		return result;
	}

	private void getSituationViolenceTypesAndModalities(Integer patientId, ViolenceReportSituationBo situation) {
		Short situationId = situation.getSituationId();
		List<Integer> situationReportIds = violenceReportRepository.getAllReportIdsByPatientIdAndSituationId(patientId, situationId);
		List<String> violenceTypes = violenceTypeRepository.getViolenceTypeNamesByReportIds(situationReportIds);
		List<String> violenceModalities = violenceModalityRepository.getViolenceModalityNamesByReportIds(situationReportIds);
		situation.setViolenceTypes(violenceTypes);
		situation.setViolenceModalities(violenceModalities);
	}

}
