package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEAllergyVo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
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