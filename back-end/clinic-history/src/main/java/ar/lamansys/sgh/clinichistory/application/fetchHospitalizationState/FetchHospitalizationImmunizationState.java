package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchHospitalizationImmunizationState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationImmunizationState.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHImmunizationRepository hchImmunizationRepository;

    public FetchHospitalizationImmunizationState(HCHImmunizationRepository hchImmunizationRepository){
        this.hchImmunizationRepository = hchImmunizationRepository;
    }

    public List<ImmunizationBo> run(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<ImmunizationVo> queryResult = hchImmunizationRepository.findGeneralState(internmentEpisodeId);
        List<ImmunizationBo> result = queryResult.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
