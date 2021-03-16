package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.documents.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.documents.service.hce.HCEHealthConditionsService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEPersonalHistoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HCEHealthConditionsExternalServiceImpl implements HCEHealthConditionsExternalService {

	private static final Logger LOG = LoggerFactory.getLogger(HCEHealthConditionsExternalServiceImpl.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

	private final HCEHealthConditionsService hceHealthConditionsService;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	public HCEHealthConditionsExternalServiceImpl(HCEHealthConditionsService hceHealthConditionsService, HCEGeneralStateMapper hceGeneralStateMapper){
		this.hceHealthConditionsService = hceHealthConditionsService;
		this.hceGeneralStateMapper = hceGeneralStateMapper;
	}

	@Override
	public List<HCEPersonalHistoryDto> getFamilyHistories(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getFamilyHistories(patientId);
		List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public List<HCEPersonalHistoryDto> getActiveProblems(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getActiveProblems(patientId);
		List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public List<HCEPersonalHistoryDto> getChronicConditions(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEPersonalHistoryBo> resultService= hceHealthConditionsService.getChronicConditions(patientId);
		List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}
}
