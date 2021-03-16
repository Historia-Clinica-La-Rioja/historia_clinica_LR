package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEMedicationDto;
import net.pladema.clinichistory.documents.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.documents.service.hce.HCEMedicationService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEMedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HCEMedicationExternalServiceImpl implements HCEMedicationExternalService {

	private static final Logger LOG = LoggerFactory.getLogger(HCEMedicationExternalServiceImpl.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

	private final HCEMedicationService hceMedicationService;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	public HCEMedicationExternalServiceImpl(HCEMedicationService hceMedicationService, HCEGeneralStateMapper hceGeneralStateMapper){
		this.hceMedicationService = hceMedicationService;
		this.hceGeneralStateMapper = hceGeneralStateMapper;
	}

	@Override
	public List<HCEMedicationDto> getMedication(Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEMedicationBo> resultService = hceMedicationService.getMedication(patientId);
		List<HCEMedicationDto> result = hceGeneralStateMapper.toListHCEMedicationDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}
}
