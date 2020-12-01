package net.pladema.clinichistory.documents.core.hce;

import net.pladema.clinichistory.documents.repository.hce.HCEAllergyIntoleranceRepository;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEAllergyVo;
import net.pladema.clinichistory.documents.service.hce.HCEAllergyService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEAllergyBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEAllergyServiceImpl implements HCEAllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEAllergyServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEAllergyIntoleranceRepository hceAllergyIntoleranceRepository;

    public HCEAllergyServiceImpl(HCEAllergyIntoleranceRepository hceAllergyIntoleranceRepository) {
        this.hceAllergyIntoleranceRepository = hceAllergyIntoleranceRepository;
    }

    @Override
    public List<HCEAllergyBo> getAllergies(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEAllergyVo> resultQuery = hceAllergyIntoleranceRepository.findAllergies(patientId);
        List<HCEAllergyBo> result = resultQuery.stream().map(HCEAllergyBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}