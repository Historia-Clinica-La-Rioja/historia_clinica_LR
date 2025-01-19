package net.pladema.violencereport.application;

import ar.lamansys.sgh.shared.domain.FilterOptionBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.domain.ViolenceReportFilterOptionBo;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceModalityRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceTypeRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GetFilters {

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceModalityRepository violenceModalityRepository;

	private ViolenceTypeRepository violenceTypeRepository;

	public ViolenceReportFilterOptionBo run(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		ViolenceReportFilterOptionBo result = new ViolenceReportFilterOptionBo();
		result.setSituations(getPatientSituationFilters(patientId));
		result.setInstitutions(getPatientInstitutionFilters(patientId));
		result.setModalities(getPatientModalityFilters(patientId));
		result.setTypes(getPatientTypeFilters(patientId));
		log.debug("Output -> {}", result);
		return result;
	}

	private List<FilterOptionBo> getPatientTypeFilters(Integer patientId) {
		return violenceTypeRepository.getTypeFilterByPatientId(patientId);
	}

	private List<FilterOptionBo> getPatientModalityFilters(Integer patientId) {
		return violenceModalityRepository.getModalityFilterByPatientId(patientId);
	}

	private List<FilterOptionBo> getPatientInstitutionFilters(Integer patientId) {
		return violenceReportRepository.getInstitutionFilterByPatientId(patientId);
	}

	private List<FilterOptionBo> getPatientSituationFilters(Integer patientId) {
		return violenceReportRepository.getSituationFilterByPatientId(patientId);
	}

}
