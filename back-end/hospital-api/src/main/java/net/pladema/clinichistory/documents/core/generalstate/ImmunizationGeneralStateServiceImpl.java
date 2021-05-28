package net.pladema.clinichistory.documents.core.generalstate;

import net.pladema.clinichistory.documents.service.generalstate.ImmunizationGeneralStateService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.HCHImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImmunizationGeneralStateServiceImpl implements ImmunizationGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(ImmunizationGeneralStateServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHImmunizationRepository hchImmunizationRepository;

    public ImmunizationGeneralStateServiceImpl(HCHImmunizationRepository hchImmunizationRepository){
        this.hchImmunizationRepository = hchImmunizationRepository;
    }

    @Override
    public List<ImmunizationBo> getImmunizationsGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<ImmunizationVo> queryResult = hchImmunizationRepository.findGeneralState(internmentEpisodeId);
        List<ImmunizationBo> result = queryResult.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
