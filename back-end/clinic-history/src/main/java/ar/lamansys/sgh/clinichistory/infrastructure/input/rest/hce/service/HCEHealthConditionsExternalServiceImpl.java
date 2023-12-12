package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEHealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEHealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEHealthConditionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HCEHealthConditionsExternalServiceImpl implements HCEHealthConditionsExternalService {

	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

	private final HCEHealthConditionsService hceHealthConditionsService;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	@Override
	public List<HCEHealthConditionDto> getFamilyHistories(Integer patientId) {
		log.debug(LOGGING_INPUT, patientId);
		List<HCEHealthConditionBo> resultService = hceHealthConditionsService.getFamilyHistories(patientId);
		List<HCEHealthConditionDto> result = hceGeneralStateMapper.toListHealthConditionDto(resultService);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public List<HCEHealthConditionDto> getSummaryProblems(Integer patientId) {
		log.debug(LOGGING_INPUT, patientId);
		List<HCEHealthConditionBo> resultService = hceHealthConditionsService.getSummaryProblems(patientId);
		List<HCEHealthConditionDto> result = hceGeneralStateMapper.toListHealthConditionDto(resultService);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

}
