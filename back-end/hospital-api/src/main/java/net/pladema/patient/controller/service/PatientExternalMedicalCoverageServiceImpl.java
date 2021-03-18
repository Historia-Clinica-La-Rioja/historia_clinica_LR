package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.mapper.PatientMedicalCoverageMapper;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientExternalMedicalCoverageServiceImpl implements  PatientExternalMedicalCoverageService{

	private static final Logger LOG = LoggerFactory.getLogger(PatientExternalMedicalCoverageServiceImpl.class);

	private static final String ONE_INPUT_PARAMETER = "Input parameters -> {}";

	public static final String OUTPUT = "Output -> {}";

	private final PatientMedicalCoverageMapper patientMedicalCoverageMapper;

	private final PatientMedicalCoverageService patientMedicalCoverageService;

	public PatientExternalMedicalCoverageServiceImpl(PatientMedicalCoverageMapper patientMedicalCoverageMapper, PatientMedicalCoverageService patientMedicalCoverageService){
		this.patientMedicalCoverageMapper = patientMedicalCoverageMapper;
		this.patientMedicalCoverageService = patientMedicalCoverageService;
	}

	@Override
	public PatientMedicalCoverageDto getCoverage(Integer patientMedicalCoverageId) {
		LOG.debug(ONE_INPUT_PARAMETER, patientMedicalCoverageId);
		PatientMedicalCoverageBo serviceResult = patientMedicalCoverageService.getCoverage(patientMedicalCoverageId).orElse(null);
		PatientMedicalCoverageDto result = patientMedicalCoverageMapper.toPatientMedicalCoverageDto(serviceResult);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<PatientMedicalCoverageDto> getActivePrivateMedicalCoverages(Integer patientId) {
		LOG.debug(ONE_INPUT_PARAMETER, patientId);
		List<PatientMedicalCoverageBo> serviceResult = patientMedicalCoverageService.getActiveCoverages(patientId);
		List<PatientMedicalCoverageDto> result = patientMedicalCoverageMapper.toListPatientMedicalCoverageDto(serviceResult);
		LOG.debug(OUTPUT, result);
		return result;
	}
}
