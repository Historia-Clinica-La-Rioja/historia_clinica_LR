package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.clinichistory.documents.core.ReasonService;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReasonExternalServiceImpl implements ReasonExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(ReasonExternalServiceImpl.class);

    private final ReasonService reasonService;

    private final SnomedMapper snomedMapper;

    public ReasonExternalServiceImpl(ReasonService reasonService, SnomedMapper snomedMapper) {
        this.reasonService = reasonService;
        this.snomedMapper = snomedMapper;
    }

    @Override
    public List<SnomedDto> addReasons(List<SnomedDto> reasons) {
        LOG.debug("Input Parameter -> reasons {}", reasons);
        List<ReasonBo> reasonBos = reasons.stream()
                .map(reason -> {
                    ReasonBo r = new ReasonBo();
                    r.setSnomed(snomedMapper.fromSnomedDto(reason));
                    return r;
                })
                .collect(Collectors.toList());
        reasonService.addReasons(reasonBos);
        LOG.debug("Output -> {}", reasons);
        return reasons;
    }
}
