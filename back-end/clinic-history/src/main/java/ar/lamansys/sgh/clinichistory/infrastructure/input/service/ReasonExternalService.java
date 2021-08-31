package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.reason.ReasonService;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReasonDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReasonExternalService {

    private final ReasonService reasonService;

    private final SnomedMapper snomedMapper;

    public ReasonExternalService(ReasonService reasonService, SnomedMapper snomedMapper) {
        this.reasonService = reasonService;
        this.snomedMapper = snomedMapper;
    }

    public List<String> addReasons(List<ReasonDto> reasons) {
        List<ReasonBo> mapped = reasons.stream()
                .map((r) -> new ReasonBo(snomedMapper.fromSnomedDto(r.getSnomed())))
                .collect(Collectors.toList());
        return reasonService.addReasons(mapped);
    }


    public List<SnomedDto> addSnomedReasons(List<SnomedDto> reasons) {
        List<ReasonBo> reasonBos = reasons.stream()
                .map(reason -> {
                    ReasonBo r = new ReasonBo();
                    r.setSnomed(snomedMapper.fromSnomedDto(reason));
                    return r;
                })
                .collect(Collectors.toList());
        reasonService.addReasons(reasonBos);
        return reasons;
    }
}
