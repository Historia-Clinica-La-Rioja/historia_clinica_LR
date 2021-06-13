package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEAllergyDto;
import net.pladema.clinichistory.documents.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.documents.service.hce.HCEAllergyService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEAllergyBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HCEAllergyExternalServiceImpl implements HCEAllergyExternalService {

	private static final Logger LOG = LoggerFactory.getLogger(HCEAllergyExternalServiceImpl.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

	private final HCEAllergyService hceAllergyService;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	public HCEAllergyExternalServiceImpl(HCEAllergyService hceAllergyService, HCEGeneralStateMapper hceGeneralStateMapper){
		this.hceAllergyService = hceAllergyService;
		this.hceGeneralStateMapper = hceGeneralStateMapper;
	}

	@Override
	public List<HCEAllergyDto> getAllergies(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEAllergyBo> resultService = hceAllergyService.getAllergies(patientId);
		List<HCEAllergyDto> result = hceGeneralStateMapper.toListHCEAllergyDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}
}
