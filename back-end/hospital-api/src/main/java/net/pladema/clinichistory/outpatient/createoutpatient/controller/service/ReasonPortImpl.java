package net.pladema.clinichistory.outpatient.createoutpatient.controller.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.ReasonExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReasonDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonPortImpl implements ReasonPort {

    private final ReasonExternalService reasonExternalService;

    public ReasonPortImpl(ReasonExternalService reasonExternalService) {
        this.reasonExternalService = reasonExternalService;
    }

    @Override
    public ReasonBo saveReason(ReasonBo reasonBo) {
        ReasonDto reasonDto = new ReasonDto(new SnomedDto(reasonBo.getSnomed().getSctid(), reasonBo.getPt()));
        reasonExternalService.addReasons(List.of(reasonDto));
        return reasonBo;

    }
}
