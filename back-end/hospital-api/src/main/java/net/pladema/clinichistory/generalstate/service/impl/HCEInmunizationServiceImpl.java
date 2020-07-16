package net.pladema.clinichistory.generalstate.service.impl;

import net.pladema.clinichistory.generalstate.repository.HCEInmunizationRepository;
import net.pladema.clinichistory.generalstate.repository.domain.HCEInmunizationHistoryVo;
import net.pladema.clinichistory.generalstate.service.HCEInmunizationService;
import net.pladema.clinichistory.generalstate.service.domain.HCEInmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEInmunizationServiceImpl implements HCEInmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEGeneralStateServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEInmunizationRepository hceInmunizationRepository;

    public HCEInmunizationServiceImpl(HCEInmunizationRepository hceInmunizationRepository) {
        this.hceInmunizationRepository = hceInmunizationRepository;
    }

    @Override
    public List<HCEInmunizationBo> getInmunizationHistory(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEInmunizationHistoryVo> resultQuery = hceInmunizationRepository.getInmunizationHistory(patientId);
        List<HCEInmunizationBo> result = resultQuery.stream().map(HCEInmunizationBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
