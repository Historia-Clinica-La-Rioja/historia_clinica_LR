package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.AllergyConditionMapper;
import net.pladema.clinichistory.hospitalization.repository.generalstate.HCHAllergyIntoleranceRepository;
import net.pladema.clinichistory.ips.service.domain.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyGeneralStateServiceImpl implements AllergyGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyGeneralStateServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";


    private final HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository;

    private final AllergyConditionMapper allergyConditionMapper;

    public AllergyGeneralStateServiceImpl(HCHAllergyIntoleranceRepository hchAllergyIntoleranceRepository,
                                          AllergyConditionMapper allergyConditionMapper){
        this.hchAllergyIntoleranceRepository = hchAllergyIntoleranceRepository;
        this.allergyConditionMapper = allergyConditionMapper;
    }

    @Override
    public List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<AllergyConditionBo> result = allergyConditionMapper.toListAllergyConditionBo(
                hchAllergyIntoleranceRepository.findGeneralState(internmentEpisodeId));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
