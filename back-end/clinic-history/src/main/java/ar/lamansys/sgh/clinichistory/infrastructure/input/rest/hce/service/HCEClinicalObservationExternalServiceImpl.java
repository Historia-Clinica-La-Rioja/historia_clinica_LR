package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2RiskFactorsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HCEClinicalObservationExternalServiceImpl implements HCEClinicalObservationExternalService{

	private static final Logger LOG = LoggerFactory.getLogger(HCEClinicalObservationExternalServiceImpl.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

	private final HCEClinicalObservationService hceClinicalObservationService;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	public HCEClinicalObservationExternalServiceImpl(HCEClinicalObservationService hceClinicalObservationService, HCEGeneralStateMapper hceGeneralStateMapper){
		this.hceClinicalObservationService = hceClinicalObservationService;
		this.hceGeneralStateMapper = hceGeneralStateMapper;
	}

	@Override
	public HCEAnthropometricDataDto getLastAnthropometricDataGeneralState(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		HCEAnthropometricDataBo resultService = hceClinicalObservationService.getLastAnthropometricDataGeneralState(patientId);
		HCEAnthropometricDataDto result = hceGeneralStateMapper.toHCEAnthropometricDataDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public HCELast2RiskFactorsDto getLast2RiskFactorsGeneralState(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		Last2HCERiskFactorsBo resultService = hceClinicalObservationService.getLast2RiskFactorsGeneralState(patientId);
		HCELast2RiskFactorsDto result = hceGeneralStateMapper.toHCELast2RiskFactorsDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}
}
