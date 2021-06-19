package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEAnthropometricDataDto;
import net.pladema.clinichistory.documents.controller.dto.HCELast2VitalSignsDto;
import net.pladema.clinichistory.documents.controller.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCEVitalSignsBo;
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
	public HCELast2VitalSignsDto getLast2VitalSignsGeneralState(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		Last2HCEVitalSignsBo resultService = hceClinicalObservationService.getLast2VitalSignsGeneralState(patientId);
		HCELast2VitalSignsDto result = hceGeneralStateMapper.toHCELast2VitalSignsDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}
}
