package net.pladema.clinichistory.outpatient.createoutpatient.controller.service;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.ReasonService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
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
    public List<String> addReasons(List<SnomedDto> reasons) {
        LOG.debug("Input Parameter -> reasons {}", reasons);
        List<ReasonBo> reasonBos = reasons.stream()
                .map(reason -> {
                    ReasonBo r = new ReasonBo();
                    r.setSnomed(snomedMapper.fromSnomedDto(reason));
                    return r;
                })
                .collect(Collectors.toList());
        List<String> result = reasonService.addReasons(reasonBos);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
