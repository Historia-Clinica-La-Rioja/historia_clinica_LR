package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchHospitalizationAllergyState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationAllergyState.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository;

    private final AllergyConditionServiceMapper allergyConditionServiceMapper;

    public FetchHospitalizationAllergyState(HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository,
                                            AllergyConditionServiceMapper allergyConditionServiceMapper){
        this.hchAllergyIntoleranceRepository = hchAllergyIntoleranceRepository;
        this.allergyConditionServiceMapper = allergyConditionServiceMapper;
    }

    public List<AllergyConditionBo> run(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<AllergyConditionBo> result = allergyConditionServiceMapper.toListAllergyConditionBo(
                hchAllergyIntoleranceRepository.findGeneralState(internmentEpisodeId));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
