package net.pladema.clinichistory.generalstate.service.impl;

import net.pladema.clinichistory.generalstate.repository.HCEMedicationStatementRepository;
import net.pladema.clinichistory.generalstate.repository.domain.HCEMedicationVo;
import net.pladema.clinichistory.generalstate.service.HCEMedicationService;
import net.pladema.clinichistory.generalstate.service.domain.HCEMedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEMedicationServiceImpl implements HCEMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEMedicationServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEMedicationStatementRepository hceMedicationStatementRepository;

    public HCEMedicationServiceImpl(HCEMedicationStatementRepository hceMedicationStatementRepository) {
        this.hceMedicationStatementRepository = hceMedicationStatementRepository;
    }

    @Override
    public List<HCEMedicationBo> getMedication(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEMedicationVo> resultQuery = hceMedicationStatementRepository.getMedication(patientId);
        List<HCEMedicationBo> result = resultQuery.stream().map(HCEMedicationBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
