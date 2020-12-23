package net.pladema.clinichistory.documents.core.generalstate;

import net.pladema.clinichistory.documents.service.generalstate.AllergyGeneralStateService;
import net.pladema.clinichistory.documents.repository.generalstate.HCHAllergyIntoleranceRepository;
import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyGeneralStateServiceImpl implements AllergyGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyGeneralStateServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository;

    private final AllergyConditionServiceMapper allergyConditionServiceMapper;

    public AllergyGeneralStateServiceImpl(HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository,
                                          AllergyConditionServiceMapper allergyConditionServiceMapper){
        this.hchAllergyIntoleranceRepository = hchAllergyIntoleranceRepository;
        this.allergyConditionServiceMapper = allergyConditionServiceMapper;
    }

    @Override
    public List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<AllergyConditionBo> result = allergyConditionServiceMapper.toListAllergyConditionBo(
                hchAllergyIntoleranceRepository.findGeneralState(internmentEpisodeId));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
