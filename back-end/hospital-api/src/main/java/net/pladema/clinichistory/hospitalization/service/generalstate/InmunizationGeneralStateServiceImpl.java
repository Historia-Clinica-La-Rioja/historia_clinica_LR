package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.HCHInmunizationRepository;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo;
import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InmunizationGeneralStateServiceImpl implements InmunizationGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InmunizationGeneralStateServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHInmunizationRepository hchInmunizationRepository;

    public InmunizationGeneralStateServiceImpl(HCHInmunizationRepository hchInmunizationRepository){
        this.hchInmunizationRepository = hchInmunizationRepository;
    }

    @Override
    public List<ImmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<InmunizationVo> queryResult = hchInmunizationRepository.findGeneralState(internmentEpisodeId);
        List<ImmunizationBo> result = queryResult.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
