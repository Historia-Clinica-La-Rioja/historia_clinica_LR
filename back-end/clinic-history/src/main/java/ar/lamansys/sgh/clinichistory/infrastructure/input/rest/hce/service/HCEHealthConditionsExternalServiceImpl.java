package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEPersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEHealthConditionsService;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
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
	public List<HCEPersonalHistoryDto> getActivePersonalHistories(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getActivePersonalHistories(patientId);
		List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

}
